package cn.zl.account.book.controller.biz.analyze;

import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.application.FundsAnalyzeAppService;
import cn.zl.account.book.controller.converter.AnalyzeConverter;
import cn.zl.account.book.controller.response.FundsOverviewResponse;
import cn.zl.account.book.controller.response.FundsTrendResponse;
import cn.zl.account.book.controller.response.NormalResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
        List<FundsTrendInfo> fundsTrendInfos = fundsAnalyzeAppService.fundsTrend(trendType);

        List<FundsTrendResponse> fundsTrendResponses = fundsTrendInfos.stream()
                .map(AnalyzeConverter::converterInfo2Resp)
                .collect(Collectors.toList());

        return NormalResponse.wrapSuccessResponse(fundsTrendResponses);
    }


}
