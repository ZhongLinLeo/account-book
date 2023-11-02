package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.controller.application.FundsRecordAppService;
import cn.zl.account.book.controller.application.FundsRecordClassifyAppService;
import cn.zl.account.book.controller.converter.AccountConverter;
import cn.zl.account.book.controller.converter.FundsRecordClassifyConverter;
import cn.zl.account.book.controller.converter.FundsRecordConverter;
import cn.zl.account.book.controller.request.FundsRecordQueryRequest;
import cn.zl.account.book.controller.request.FundsRecordRequest;
import cn.zl.account.book.controller.response.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @Resource
    private FundsRecordClassifyAppService fundsRecordClassifyAppService;

    @Resource
    private AccountAppService accountAppService;

    @GetMapping("pagination")
    public PageBaseResponse<FundsRecordResponse> paginationFundsRecord(@Valid FundsRecordQueryRequest pagination) {
        Page<FundsRecordInfo> fundsRecords = fundsRecordAppService.paginationFundsRecord(pagination);

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

    @PostMapping()
    public NormalResponse<Boolean> recordFunds(@RequestBody @Valid FundsRecordRequest fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.recordFunds(fundsRecordInfo);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @PutMapping("{recordId}")
    public NormalResponse<Boolean> modifyFundsRecord(@PathVariable @NotNull(message = "记录ID不能为空") Long recordId,
                                                     @RequestBody @Valid FundsRecordRequest fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordInfo.setFundsRecordId(recordId);
        fundsRecordAppService.modifyFundsRecord(fundsRecordInfo);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public NormalResponse<Boolean> delFundsRecord(@PathVariable @NotNull(message = "记录ID不能为空") Long recordId) {
        fundsRecordAppService.delFundsRecord(recordId);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @PostMapping("import")
    public NormalResponse<Boolean> importFundsRecord(@RequestParam("file") MultipartFile excelFile) {
        fundsRecordAppService.importFundsRecord(excelFile);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }
}
