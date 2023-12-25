package cn.zl.account.book.controller;

import cn.zl.account.book.info.AccountInfo;
import cn.zl.account.book.info.AccountTransferInfo;
import cn.zl.account.book.application.AccountAppService;
import cn.zl.account.book.converter.AccountConverter;
import cn.zl.account.book.view.request.AccountRequest;
import cn.zl.account.book.view.request.AccountOperateRequest;
import cn.zl.account.book.view.response.AccountInfoResponse;
import cn.zl.account.book.view.response.NormalResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * account controller
 *
 * @author create by leo.zl on 2023/8/17
 */
@RestController
@RequestMapping("account")
@CrossOrigin
public class AccountController {

    @Resource
    private AccountAppService accountAppService;

    /**
     * list all account info
     */
    @GetMapping("list")
    public NormalResponse<List<AccountInfoResponse>> listAccount() {
        List<AccountInfo> accountInfos = accountAppService.listAccount();

        List<AccountInfoResponse> responseList = accountInfos.stream()
                .map(AccountConverter::info2Resp)
                .collect(Collectors.toList());
        return NormalResponse.wrapSuccessResponse(responseList);
    }

    /**
     * list account
     *
     * @param accountReq account base info
     */
    @PostMapping()
    public NormalResponse<Boolean> createAccount(@RequestBody AccountRequest accountReq) {
        final AccountInfo accountInfo = AccountConverter.req2Info(accountReq);

        accountAppService.createAccount(accountInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    /**
     * list account
     *
     * @param accountId  account id
     * @param accountReq account base info
     */
    @PutMapping("{accountId}")
    public NormalResponse<Boolean> modifyAccount(@PathVariable Long accountId, @RequestBody AccountRequest accountReq) {
        final AccountInfo accountInfo = AccountConverter.req2Info(accountReq);
        accountInfo.setAccountId(accountId);
        accountAppService.modifyAccount(accountInfo);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    /**
     * list account
     *
     * @param accountId account id
     */
    @DeleteMapping("{accountId}")
    public NormalResponse<Boolean> delAccount(@PathVariable Long accountId) {
        accountAppService.delAccount(accountId);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    /**
     * transfer
     *
     * @param accountId account id
     * @param transferRequest transfer req
     */
    @PutMapping("{accountId}/transfer")
    public NormalResponse<Boolean> transfer(@PathVariable Long accountId,
                                            @RequestBody AccountOperateRequest transferRequest) {
        transferRequest.setAccountId(accountId);
        AccountTransferInfo transferInfo = AccountConverter.req2Info(transferRequest);

        accountAppService.transfer(transferInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    /**
     * transfer
     *
     * @param accountId account id
     * @param repaymentReq transfer req
     */
    @PutMapping("{accountId}/repayment")
    public NormalResponse<Boolean> repayment(@PathVariable Long accountId,
                                             @RequestBody AccountOperateRequest repaymentReq) {
        repaymentReq.setAccountId(accountId);
        AccountTransferInfo transferInfo = AccountConverter.req2Info(repaymentReq);

        accountAppService.repayment(transferInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }
}
