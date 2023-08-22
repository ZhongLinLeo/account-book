package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.application.FundsRecordClassifyAppService;
import cn.zl.account.book.controller.converter.FundsRecordClassifyConverter;
import cn.zl.account.book.controller.request.FundsRecordClassifyRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordClassifyResponseDTO;
import cn.zl.account.book.controller.response.NormalResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController("funds_record_classify")
public class FundsRecordClassifyController {

    @Resource
    private FundsRecordClassifyAppService fundsRecordClassifyAppService;

    @PostMapping()
    public NormalResponseDTO<Boolean> addClassify(FundsRecordClassifyRequestDTO fundsRecordClassifyReq) {
        FundsRecordClassifyInfo fundsRecordClassifyInfo = FundsRecordClassifyConverter.req2Info(fundsRecordClassifyReq);

        fundsRecordClassifyAppService.addClassify(fundsRecordClassifyInfo);

        return NormalResponseDTO.wrapSuccessResponse(Boolean.TRUE);
    }


    @PutMapping()
    public NormalResponseDTO<Boolean> modifyClassify(FundsRecordClassifyRequestDTO fundsRecordClassifyReq) {
        FundsRecordClassifyInfo fundsRecordClassifyInfo = FundsRecordClassifyConverter.req2Info(fundsRecordClassifyReq);

        fundsRecordClassifyAppService.modifyClassify(fundsRecordClassifyInfo);


        return NormalResponseDTO.wrapSuccessResponse(Boolean.TRUE);
    }

    @DeleteMapping("{classifyId}")
    public NormalResponseDTO<Boolean> delClassify(@PathVariable Long classifyId) {
        fundsRecordClassifyAppService.delClassify(classifyId);
        return NormalResponseDTO.wrapSuccessResponse(Boolean.TRUE);
    }

    @DeleteMapping("list")
    public NormalResponseDTO<List<FundsRecordClassifyResponseDTO>> listClassify() {

        List<FundsRecordClassifyInfo> classifyInfos = fundsRecordClassifyAppService.listClassify();
        List<FundsRecordClassifyResponseDTO> responseList = classifyInfos.stream()
                .map(FundsRecordClassifyConverter::info2Resp)
                .collect(Collectors.toList());

        return NormalResponseDTO.wrapSuccessResponse(responseList);
    }


}
