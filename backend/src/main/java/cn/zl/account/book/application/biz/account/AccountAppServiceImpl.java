package cn.zl.account.book.application.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * account app
 *
 * @author lin.zl
 */
@Service
@Slf4j
public class AccountAppServiceImpl implements AccountAppService {

    @Resource
    private AccountDomainService accountDomainService;

    @Override
    public void createAccount(AccountInfo accountInfo) {
        accountInfo.setAccountIncome(0L);
        accountInfo.setAccountExpenditure(0L);
        accountDomainService.createAccount(accountInfo);
    }

    @Override
    public List<AccountInfo> listAccount() {
        return accountDomainService.listAccounts();
    }

    @Override
    public void modifyAccount(AccountInfo accountInfo) {
        accountDomainService.modifyAccount(accountInfo);
    }

    @Override
    public void delAccount(Long accountId) {
        accountDomainService.delAccount(accountId);
    }
}
