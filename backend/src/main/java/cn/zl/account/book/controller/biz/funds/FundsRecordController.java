package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.FundsRecordAppService;
import cn.zl.account.book.controller.converter.FundsRecordConverter;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.request.FundsRecordRequestDTO;
import cn.zl.account.book.controller.request.PaginationFundsRecordRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;
import cn.zl.account.book.controller.response.NormalResponseDTO;
import cn.zl.account.book.controller.response.PageBaseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("funds_record")
public class FundsRecordController {

    @Resource
    private FundsRecordAppService fundsRecordAppService;


    @GetMapping()
    public PageBaseResponseDTO<FundsRecordResponseDTO> paginationFundsRecord(PaginationFundsRecordRequestDTO paginationReq) {
        Page<FundsRecordInfo> fundsRecords = fundsRecordAppService.paginationFundsRecord(paginationReq);

        List<FundsRecordResponseDTO> content = fundsRecords.get()
                .map(FundsRecordConverter::info2Resp)
                .collect(Collectors.toList());

        return PageBaseResponseDTO.wrapSuccessPageResponse(paginationReq.getPageSize(), paginationReq.getPageNumber(),
                fundsRecords.getTotalElements(), content);
    }

    @PostMapping()
    public NormalResponseDTO<Boolean> recordFunds(FundsRecordRequestDTO fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.recordFunds(fundsRecordInfo);
        return NormalResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PutMapping()
    public NormalResponseDTO<Boolean> modifyFundsRecord(FundsRecordRequestDTO fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.modifyFundsRecord(fundsRecordInfo);

        return NormalResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public NormalResponseDTO<Boolean> delFundsRecord(@PathVariable Long recordId) {
        fundsRecordAppService.delFundsRecord(recordId);
        return NormalResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PostMapping("import")
    public NormalResponseDTO<Boolean> importFundsRecord() {
        // todo

        return NormalResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }


}
