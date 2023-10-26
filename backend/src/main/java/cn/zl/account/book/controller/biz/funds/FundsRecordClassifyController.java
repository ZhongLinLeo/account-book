package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.application.FundsRecordClassifyAppService;
import cn.zl.account.book.controller.converter.FundsRecordClassifyConverter;
import cn.zl.account.book.controller.request.FundsClassifyQueryRequest;
import cn.zl.account.book.controller.request.FundsRecordClassifyRequest;
import cn.zl.account.book.controller.response.FundsRecordClassifyResponse;
import cn.zl.account.book.controller.response.NormalResponse;
import cn.zl.account.book.controller.response.PageBaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController()
@RequestMapping("funds_record_classify")
@CrossOrigin
public class FundsRecordClassifyController {

    @Resource
    private FundsRecordClassifyAppService fundsRecordClassifyAppService;

    @PostMapping()
    public NormalResponse<Boolean> addClassify(@Valid @RequestBody FundsRecordClassifyRequest fundsRecordClassifyReq) {
        FundsRecordClassifyInfo fundsRecordClassifyInfo = FundsRecordClassifyConverter.req2Info(fundsRecordClassifyReq);

        fundsRecordClassifyAppService.addClassify(fundsRecordClassifyInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @PutMapping("{classifyId}")
    public NormalResponse<Boolean> modifyClassify(@PathVariable @NotBlank(message = "classifyId不能为空") Long classifyId,
                                                  @Valid @RequestBody FundsRecordClassifyRequest fundsRecordClassifyReq) {
        FundsRecordClassifyInfo fundsRecordClassifyInfo = FundsRecordClassifyConverter.req2Info(fundsRecordClassifyReq);
        fundsRecordClassifyInfo.setClassifyId(classifyId);
        fundsRecordClassifyAppService.modifyClassify(fundsRecordClassifyInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @DeleteMapping("{classifyId}")
    public NormalResponse<Boolean> delClassify(@PathVariable @NotBlank(message = "classifyId不能为空") Long classifyId) {
        fundsRecordClassifyAppService.delClassify(classifyId);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @GetMapping("list")
    public NormalResponse<List<FundsRecordClassifyResponse>> listClassify() {
        List<FundsRecordClassifyInfo> classifyInfos = fundsRecordClassifyAppService.listClassify();
        List<FundsRecordClassifyResponse> responseList = classifyInfos.stream()
                .map(FundsRecordClassifyConverter::info2Resp)
                .collect(Collectors.toList());

        return NormalResponse.wrapSuccessResponse(responseList);
    }

    @GetMapping("pagination")
    public PageBaseResponse<FundsRecordClassifyResponse> paginationClassify(@Valid FundsClassifyQueryRequest pageQuery) {
        String keyword = pageQuery.getFundsClassifyNameKeyword();
        if (StringUtils.isBlank(keyword)) {
            pageQuery.setFundsClassifyNameKeyword(null);
        }
        Page<FundsRecordClassifyInfo> fundsClassifyInfos = fundsRecordClassifyAppService.paginationClassify(pageQuery);
        List<FundsRecordClassifyResponse> responseList = fundsClassifyInfos.stream()
                .map(FundsRecordClassifyConverter::info2Resp)
                .collect(Collectors.toList());

        return PageBaseResponse.wrapSuccessPageResponse(pageQuery.getPageSize(), pageQuery.getPageNumber(),
                fundsClassifyInfos.getTotalElements(), responseList);
    }
}
