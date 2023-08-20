package cn.zl.account.book.controller.biz.account;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.controller.converter.AccountConverter;
import cn.zl.account.book.controller.dto.AccountInfoDTO;
import cn.zl.account.book.controller.request.AccountRequest;
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
public class AccountController {

    @Resource
    private AccountAppService accountAppService;

    /**
     * list all account info
     */
    @GetMapping("list")
    public List<AccountInfoDTO> listAccount() {

        List<AccountInfo> accountInfos = accountAppService.listAccount();

       return accountInfos.stream().map(AccountConverter::accountInfo2AccountResp).collect(Collectors.toList());
    }

    /**
     * list account
     *
     * @param accountReq account base info
     */
    @PostMapping()
    public Boolean createAccount(@RequestBody AccountRequest accountReq) {
        final AccountInfo accountInfo = AccountConverter.accountRequest2AccountInfo(accountReq);

        return accountAppService.createAccount(accountInfo);
    }

    /**
     * list account
     *
     * @param accountId  account id
     * @param accountReq account base info
     */
    @PutMapping("{accountId}")
    public Boolean modifyAccount(@PathVariable Long accountId, @RequestBody AccountRequest accountReq) {
        final AccountInfo accountInfo = AccountConverter.accountRequest2AccountInfo(accountReq);
        accountInfo.setAccountId(accountId);
        return accountAppService.modifyAccount(accountInfo);
    }

    /**
     * list account
     *
     * @param accountId account id
     */
    @DeleteMapping("{accountId}")
    public Boolean delAccount(@PathVariable Long accountId) {
        return accountAppService.delAccount(accountId);
    }
}
