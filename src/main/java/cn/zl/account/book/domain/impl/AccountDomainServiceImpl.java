package cn.zl.account.book.domain.impl;

import cn.zl.account.book.domain.AccountDomainService;
import cn.zl.account.book.info.AccountInfo;
import cn.zl.account.book.info.AccountTransferInfo;
import cn.zl.account.book.enums.ClassifyTypeEnum;
import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.architecture.BizException;
import cn.zl.account.book.infrastructure.repository.AccountRepository;
import cn.zl.account.book.infrastructure.repository.UserRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import cn.zl.account.book.util.BeanCopyUtils;
import cn.zl.account.book.util.SnowIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
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
        final long accountId = SnowIdUtils.nextId();
        accountEntity.setAccountId(accountId);

        final LocalDateTime now = LocalDateTime.now();
        accountEntity.setCreateTime(now);
        accountEntity.setModifyTime(now);
        accountEntity.setInvalid(0);

        accountRepository.save(accountEntity);
    }

    @Override
    public void modifyAccount(AccountInfo accountInfo, AccountEntity accountEntity) {
        BeanUtils.copyProperties(accountInfo, accountEntity, BeanCopyUtils.getNullPropertyNames(accountInfo));

        final LocalDateTime now = LocalDateTime.now();
        accountEntity.setModifyTime(now);
        accountRepository.save(accountEntity);
    }

    @Override
    public void delAccount(Long accountId) {
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountId);

        if (!originOptional.isPresent()) {
            return;
        }
        AccountEntity entity = originOptional.get();
        entity.setInvalid(1);
        entity.setModifyTime(LocalDateTime.now());
        accountRepository.save(entity);
    }

    @Override
    public void transfer(AccountTransferInfo transferInfo, AccountEntity sourceAccount, AccountEntity targetAccount) {
        Long transferBalance = transferInfo.getTransferBalance();

        // modify account balance
        Long accountBalance = sourceAccount.getAccountBalance();
        sourceAccount.setAccountBalance(accountBalance - transferBalance);
        accountRepository.save(sourceAccount);

        targetAccount.setAccountBalance(targetAccount.getAccountBalance() + transferBalance);
        accountRepository.save(targetAccount);
    }

    @Override
    public void repayment(AccountTransferInfo repaymentInfo, AccountEntity sourceAccount, AccountEntity targetAccount) {
        Long transferBalance = repaymentInfo.getTransferBalance();

        // modify account balance
        Long accountBalance = sourceAccount.getAccountBalance();
        sourceAccount.setAccountBalance(accountBalance - transferBalance);
        accountRepository.save(sourceAccount);

        targetAccount.setAccountBalance(targetAccount.getAccountBalance() + transferBalance);
        accountRepository.save(targetAccount);
    }

    @Override
    public void transaction(Long accountId, Long transactionBalance, ClassifyTypeEnum classifyTypeEnum) {
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountId);
        final AccountEntity accountEntity =
                originOptional.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        // modify account balance
        if (Objects.equals(classifyTypeEnum,ClassifyTypeEnum.INCOME)){
            accountEntity.setAccountBalance(accountEntity.getAccountBalance() + transactionBalance);
            accountEntity.setAccountIncome(transactionBalance);
        }else {
            accountEntity.setAccountBalance(accountEntity.getAccountBalance() - transactionBalance);
            accountEntity.setAccountExpenditure(accountEntity.getAccountExpenditure() + transactionBalance);
        }

        accountRepository.save(accountEntity);
    }

    @Override
    public void transaction(Long accountId, Long transactionBalance) {
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountId);
        final AccountEntity accountEntity =
                originOptional.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        // modify account balance
        accountEntity.setAccountBalance(accountEntity.getAccountBalance() - transactionBalance);
        accountEntity.setAccountExpenditure(accountEntity.getAccountExpenditure() + transactionBalance);

        accountRepository.save(accountEntity);
    }
}
