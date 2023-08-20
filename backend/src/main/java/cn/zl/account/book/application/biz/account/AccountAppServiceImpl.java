package cn.zl.account.book.application.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public Boolean createAccount(AccountInfo accountInfo) {
        return accountDomainService.createAccount(accountInfo);
    }

    @Override
    public List<AccountInfo> listAccount() {
        final List<AccountEntity> allAccount = accountRepository.findAll();
        if (CollectionUtils.isEmpty(allAccount)){
            return Collections.emptyList();
        }

       return allAccount.stream().map(AccountEntityConverter::accountEntity2AccountInfo).collect(Collectors.toList());
    }

    @Override
    public Boolean modifyAccount(AccountInfo accountInfo) {
        return accountDomainService.modifyAccount(accountInfo);
    }

    @Override
    public Boolean delAccount(Long accountId) {
        return accountDomainService.delAccount(accountId);
    }
}
