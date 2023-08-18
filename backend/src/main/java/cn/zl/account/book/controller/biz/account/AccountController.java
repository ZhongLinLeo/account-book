package cn.zl.account.book.controller.biz.account;

import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.controller.request.AccountRequest;
import cn.zl.account.book.controller.dto.AccountInfoDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
        return null;
    }

    /**
     * list account
     *
     * @param accountReq account base info
     */
    @PostMapping()
    public Boolean createAccount(@RequestBody AccountRequest accountReq) {



        return null;
    }

    /**
     * list account
     *
     * @param accountId  account id
     * @param accountReq account base info
     */
    @PutMapping("{accountId}")
    public Boolean modifyAccount(@PathVariable Long accountId, @RequestBody AccountRequest accountReq) {

        return null;
    }

    /**
     * list account
     *
     * @param accountId account id
     */
    @DeleteMapping("{accountId}")
    public Boolean delAccount(@PathVariable Long accountId) {

        return null;
    }
}
