package cn.zl.account.book.domain.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.domain.utils.SnowIdUtil;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.biz.user.UserRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private UserRepository userRepository;

    @Override
    public List<AccountInfo> listAccounts() {
        final List<AccountEntity> allAccount = accountRepository.findAccountEntities();
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
    public void createAccount(AccountInfo accountInfo) {
        final AccountEntity accountEntity = AccountEntityConverter.accountInfo2AccountEntity(accountInfo);

        // generate account id
        final long accountId = SnowIdUtil.nextId();
        accountEntity.setAccountId(accountId);

        final LocalDateTime now = LocalDateTime.now();
        accountEntity.setCreateTime(now);
        accountEntity.setModifyTime(now);
        accountEntity.setInvalid(0);

        accountRepository.save(accountEntity);
    }

    @Override
    public void modifyAccount(AccountInfo accountInfo) {
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountInfo.getAccountId());

        if (!originOptional.isPresent()) {
            return;
        }

        final AccountEntity origin = originOptional.get();
        final String accountName = accountInfo.getAccountName();
        if (StringUtils.isNoneBlank(accountName)) {
            origin.setAccountName(accountName);
        }

        final String accountDescribe = accountInfo.getAccountDescribe();
        if (StringUtils.isNoneBlank(accountDescribe)){
            origin.setAccountDescribe(accountDescribe);
        }

        final Long accountOwnershipId = accountInfo.getAccountOwnershipId();
        if (Objects.nonNull(accountOwnershipId)){
            origin.setAccountOwnershipId(accountOwnershipId);
        }

        final Long accountBalance = accountInfo.getAccountBalance();
        if (Objects.nonNull(accountBalance)){
            origin.setAccountBalance(accountBalance);
        }
        final LocalDateTime now = LocalDateTime.now();
        origin.setModifyTime(now);
        accountRepository.save(origin);
    }

    @Override
    public void delAccount(Long accountId) {
        AccountEntity entity = new AccountEntity();
        entity.setInvalid(1);
        entity.setAccountId(accountId);
        accountRepository.save(entity);
    }
}
