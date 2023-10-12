package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.application.FundsRecordClassifyAppService;
import cn.zl.account.book.controller.converter.FundsRecordClassifyConverter;
import cn.zl.account.book.controller.request.FundsRecordClassifyRequest;
import cn.zl.account.book.controller.response.FundsRecordClassifyResponse;
import cn.zl.account.book.controller.response.NormalResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public NormalResponse<Boolean> addClassify(FundsRecordClassifyRequest fundsRecordClassifyReq) {
        FundsRecordClassifyInfo fundsRecordClassifyInfo = FundsRecordClassifyConverter.req2Info(fundsRecordClassifyReq);

        fundsRecordClassifyAppService.addClassify(fundsRecordClassifyInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }


    @PutMapping()
    public NormalResponse<Boolean> modifyClassify(FundsRecordClassifyRequest fundsRecordClassifyReq) {
        FundsRecordClassifyInfo fundsRecordClassifyInfo = FundsRecordClassifyConverter.req2Info(fundsRecordClassifyReq);

        fundsRecordClassifyAppService.modifyClassify(fundsRecordClassifyInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @DeleteMapping("{classifyId}")
    public NormalResponse<Boolean> delClassify(@PathVariable Long classifyId) {
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
}
