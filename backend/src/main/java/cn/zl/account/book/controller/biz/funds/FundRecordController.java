package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.FundsRecordAppService;
import cn.zl.account.book.controller.converter.FundsRecordConverter;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.request.FundsRecordRequestDTO;
import cn.zl.account.book.controller.request.PaginationFundsRecordRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;
import cn.zl.account.book.controller.response.PageResponseDTO;
import cn.zl.account.book.controller.response.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("fund_record")
public class FundRecordController {

    @Resource
    private FundsRecordAppService fundsRecordAppService;


    @GetMapping()
    public PageResponseDTO<FundsRecordResponseDTO> paginationFundsRecord(PaginationFundsRecordRequestDTO paginationReq) {

       Page<FundsRecordInfo> fundsRecords = fundsRecordAppService.paginationFundsRecord(paginationReq);

        List<FundsRecordResponseDTO> content = fundsRecords.get()
                .map(FundsRecordConverter::fundsRecordInfo2FundsRecord)
                .collect(Collectors.toList());
        return PageResponseDTO.wrapPageResponse(ResponseStatusEnum.SUCCESS, fundsRecords, content);
    }

    @PostMapping()
    public ResponseDTO<Boolean> recordFunds(FundsRecordRequestDTO fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.fundsRecordReq2FundsRecordInfo(fundsRecordReq);
        fundsRecordAppService.recordFunds(fundsRecordInfo);
        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PutMapping()
    public ResponseDTO<Boolean> modifyFundRecord(FundsRecordRequestDTO fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.fundsRecordReq2FundsRecordInfo(fundsRecordReq);
        fundsRecordAppService.modifyFundRecord(fundsRecordInfo);

        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public ResponseDTO<Boolean> delFundRecord(@PathVariable Long recordId) {
        fundsRecordAppService.delFundRecord(recordId);
        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PostMapping("import")
    public ResponseDTO<Boolean> importFundRecords() {
        // todo

        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }


}
