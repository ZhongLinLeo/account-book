package cn.zl.account.book.application.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.infrastructure.architecture.BizException;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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


    @Resource
    private AccountRepository accountRepository;


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
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountInfo.getAccountId());

        final AccountEntity accountEntity =
                originOptional.orElseThrow(() -> new BizException(ResponseStatusEnum.FUNDS_RECORD_NONE_EXIST));

        accountDomainService.modifyAccount(accountInfo,accountEntity);
    }

    @Override
    public void delAccount(Long accountId) {
        accountDomainService.delAccount(accountId);
    }
}
