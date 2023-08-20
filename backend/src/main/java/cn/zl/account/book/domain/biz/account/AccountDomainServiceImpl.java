package cn.zl.account.book.domain.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.utils.SnowIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * account domain service
 *
 * @author lin.zl
 */
@Service
@Slf4j
public class AccountDomainServiceImpl implements AccountDomainService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public Boolean createAccount(AccountInfo accountInfo) {
        final AccountEntity accountEntity = AccountEntityConverter.accountInfo2AccountEntity(accountInfo);

        // generate account id
        final long accountId = SnowIdUtil.nextId();
        accountEntity.setAccountId(accountId);

        final LocalDateTime now = LocalDateTime.now();
        accountEntity.setCreateTime(now);
        accountEntity.setModifyTime(now);
        accountEntity.setInvalid(0);

        accountRepository.save(accountEntity);

        return Boolean.TRUE;
    }

    @Override
    public Boolean modifyAccount(AccountInfo accountInfo) {

        final AccountEntity accountEntity = AccountEntityConverter.accountInfo2AccountEntity(accountInfo);

        final LocalDateTime now = LocalDateTime.now();
        accountEntity.setModifyTime(now);
        accountRepository.save(accountEntity);

        return Boolean.TRUE;
    }

    @Override
    public Boolean delAccount(Long accountId) {
        accountRepository.deleteById(accountId);
        return Boolean.TRUE;
    }
}
