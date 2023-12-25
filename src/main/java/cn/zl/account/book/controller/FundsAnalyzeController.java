package cn.zl.account.book.controller;

import cn.zl.account.book.application.AccountAppService;
import cn.zl.account.book.application.FundsRecordAppService;
import cn.zl.account.book.application.FundsRecordClassifyAppService;
import cn.zl.account.book.converter.AccountConverter;
import cn.zl.account.book.converter.AnalyzeConverter;
import cn.zl.account.book.converter.FundsRecordClassifyConverter;
import cn.zl.account.book.converter.FundsRecordConverter;
import cn.zl.account.book.enums.TrendAnalyzeEnum;
import cn.zl.account.book.info.*;
import cn.zl.account.book.application.FundsAnalyzeAppService;
import cn.zl.account.book.view.request.FundsRecordQueryRequest;
import cn.zl.account.book.view.request.PageFundsComposeRequest;
import cn.zl.account.book.view.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资金分析
 *
 * @author lin.zl
 */
@RestController
@RequestMapping("analyze")
@CrossOrigin
public class FundsAnalyzeController {

    @Resource
    private FundsAnalyzeAppService fundsAnalyzeAppService;

    @Resource
    private FundsRecordAppService fundsRecordAppService;

    @Resource
    private AccountAppService accountAppService;

    @Resource
    private FundsRecordClassifyAppService fundsRecordClassifyAppService;

    /**
     * 资金概览
     * <p>
     * 1、收支概览（总、年、月、周）
     * 2、资产与负载
     *
     * @return 概览信息
     */
    @GetMapping("overview")
    public NormalResponse<FundsOverviewResponse> fundsOverview() {
        FundsOverviewInfo fundsOverviewInfo = fundsAnalyzeAppService.fundsOverview();

        return NormalResponse.wrapSuccessResponse(AnalyzeConverter.converterInfo2Resp(fundsOverviewInfo));
    }

    /**
     * 资金趋势
     * <p>
     * 收支趋势（总、年、月、周）
     *
     * @return 趋势信息
     */
    @GetMapping("trend")
    public NormalResponse<List<FundsTrendResponse>> fundsTrend(@Param("trendType") String trendType) {
        TrendAnalyzeEnum trendAnalyzeEnum = TrendAnalyzeEnum.valueOf(trendType);
        List<FundsTrendInfo> fundsTrendInfos = fundsAnalyzeAppService.fundsTrend(trendAnalyzeEnum);

        List<FundsTrendResponse> fundsTrendResponses = fundsTrendInfos.stream()
                .map(AnalyzeConverter::converterInfo2Resp)
                .collect(Collectors.toList());

        return NormalResponse.wrapSuccessResponse(fundsTrendResponses);
    }

    /**
     * 收支构成
     *
     * @return 构成信息
     */
    @GetMapping("compose")
    public NormalResponse<FundsComposeResponse> fundsCompose(@Param("trendType") String trendType) {

        TrendAnalyzeEnum trendAnalyzeEnum = TrendAnalyzeEnum.valueOf(trendType);
        FundsComposeInfo fundsComposeInfo = fundsAnalyzeAppService.fundsCompose(trendAnalyzeEnum);

        return NormalResponse.wrapSuccessResponse(AnalyzeConverter.converterInfo2Resp(fundsComposeInfo));
    }

    /**
     * 收支Top
     *
     * @return Top信息
     */
    @GetMapping("top")
    public NormalResponse<FundsRecordTopResponse> fundsTop(@Param("trendType") String trendType) {

        TrendAnalyzeEnum trendAnalyzeEnum = TrendAnalyzeEnum.valueOf(trendType);
        FundsRecordTopInfo fundsRecordTopInfo = fundsAnalyzeAppService.fundsTops(trendAnalyzeEnum);

        return NormalResponse.wrapSuccessResponse(AnalyzeConverter.converterInfo2Resp(fundsRecordTopInfo));
    }


    @GetMapping("pagination/compose")
    public PageBaseResponse<FundsRecordResponse> paginationFundsRecord(@Valid PageFundsComposeRequest request) {

        PageRequest pageRequest = PageRequest.of(request.getCurrent(), request.getPageSize(),
                Sort.by(Sort.Direction.fromString(request.getOrder()), request.getSortFiled()));

        Page<FundsRecordInfo> fundsRecords = fundsRecordAppService.paginationFundsRecord(pageRequest,FundsRecordConverter.req2Info(request));

        List<FundsRecordResponse> content = fundsRecords.get()
                .map(recordInfo -> {
                    FundsRecordResponse fundsRecordResponse = FundsRecordConverter.info2Resp(recordInfo);
                    FundsRecordClassifyInfo classify = fundsRecordClassifyAppService
                            .findClassify(recordInfo.getFundsRecordClassifyId());
                    FundsRecordClassifyResponse classifyResponse = FundsRecordClassifyConverter.info2Resp(classify);
                    fundsRecordResponse.setClassifyInfo(classifyResponse);

                    AccountInfo accountInfo = accountAppService.findAccountInfo(recordInfo.getFundsAccountId());
                    AccountInfoResponse accountInfoResponse = AccountConverter.info2Resp(accountInfo);
                    fundsRecordResponse.setAccountInfo(accountInfoResponse);

                    return fundsRecordResponse;
                })
                .collect(Collectors.toList());
        return PageBaseResponse.wrapSuccessPageResponse(fundsRecords.getTotalElements(), content);

    }


}
