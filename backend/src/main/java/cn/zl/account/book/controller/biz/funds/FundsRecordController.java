package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.FundsRecordAppService;
import cn.zl.account.book.controller.converter.FundsRecordConverter;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.request.FundsRecordRequest;
import cn.zl.account.book.controller.request.FundsRecordQueryRequest;
import cn.zl.account.book.controller.response.FundsRecordResponse;
import cn.zl.account.book.controller.response.NormalResponse;
import cn.zl.account.book.controller.response.PageBaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("funds_record")
@CrossOrigin
public class FundsRecordController {

    @Resource
    private FundsRecordAppService fundsRecordAppService;


    @GetMapping("pagination")
    public PageBaseResponse<FundsRecordResponse> paginationFundsRecord(FundsRecordQueryRequest pagination) {
        Page<FundsRecordInfo> fundsRecords = fundsRecordAppService.paginationFundsRecord(pagination);

        List<FundsRecordResponse> content = fundsRecords.get()
                .map(FundsRecordConverter::info2Resp)
                .collect(Collectors.toList());

        return PageBaseResponse.wrapSuccessPageResponse(pagination.getPageSize(), pagination.getPageNumber(),
                fundsRecords.getTotalElements(), content);
    }

    @PostMapping()
    public NormalResponse<Boolean> recordFunds(FundsRecordRequest fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.recordFunds(fundsRecordInfo);
        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PutMapping()
    public NormalResponse<Boolean> modifyFundsRecord(FundsRecordRequest fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.modifyFundsRecord(fundsRecordInfo);

        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public NormalResponse<Boolean> delFundsRecord(@PathVariable Long recordId) {
        fundsRecordAppService.delFundsRecord(recordId);
        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PostMapping("import")
    public NormalResponse<Boolean> importFundsRecord(@RequestParam("file") MultipartFile excelFile) throws IOException {
        fundsRecordAppService.importFundsRecord(excelFile);
        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }


}
