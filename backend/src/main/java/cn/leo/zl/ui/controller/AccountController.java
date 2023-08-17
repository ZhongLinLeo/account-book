package cn.leo.zl.ui.controller;

import cn.leo.zl.ui.dto.request.AccountReqDTO;
import cn.leo.zl.ui.dto.response.AccountRespDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * account controller
 *
 * @author create by leo.zl on 2023/8/17
 */
@RestController
@RequestMapping("account")
public class AccountController {

    /**
     * list all account info
     */
    @GetMapping("list")
    public List<AccountRespDTO> listAccount() {
        return null;
    }

    /**
     * list account
     *
     * @param accountReq account base info
     */
    @PostMapping()
    public Boolean createAccount(@RequestBody AccountReqDTO accountReq) {

        return null;
    }

    /**
     * list account
     *
     * @param accountId  account id
     * @param accountReq account base info
     */
    @PutMapping("{accountId}")
    public Boolean modifyAccount(@PathVariable Long accountId, @RequestBody AccountReqDTO accountReq) {

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
