package cn.zl.account.book.application.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.biz.user.UserRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private UserRepository userRepository;

    @Resource
    private AccountRepository accountRepository;


    @Override
    public void createAccount(AccountInfo accountInfo) {
        accountDomainService.createAccount(accountInfo);
    }

    @Override
    public List<AccountInfo> listAccount() {
        final List<AccountEntity> allAccount = accountRepository.findAll();
        if (CollectionUtils.isEmpty(allAccount)) {
            return Collections.emptyList();
        }

        List<UserEntity> userEntities = userRepository.findAll();
        Map<Long, String> userMap = userEntities.stream()
                .collect(Collectors.toMap(UserEntity::getUserId, UserEntity::getUserName, (o, n) -> n));

        return allAccount.stream().map(account -> {
            AccountInfo accountInfo = AccountEntityConverter.accountEntity2AccountInfo(account);
            Long accountOwnershipId = account.getAccountOwnershipId();
            accountInfo.setAccountOwner(userMap.get(accountOwnershipId));
            accountInfo.setAccountOwnershipId(accountOwnershipId);
            return accountInfo;
        }).collect(Collectors.toList());
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
