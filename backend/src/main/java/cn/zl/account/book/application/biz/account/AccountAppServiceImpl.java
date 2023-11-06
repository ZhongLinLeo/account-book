package cn.zl.account.book.application.biz.account;

import cn.zl.account.book.application.domain.AccountDomainService;
import cn.zl.account.book.application.domain.FundsRecordDomainService;
import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.application.info.AccountTransferInfo;
import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.AccountAppService;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.infrastructure.architecture.BizAssert;
import cn.zl.account.book.infrastructure.architecture.BizException;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.biz.user.UserRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cn.zl.account.book.application.enums.DefaultClassifyEnum.*;

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
    private FundsRecordDomainService fundsRecordDomainService;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private UserRepository userRepository;

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

        accountDomainService.modifyAccount(accountInfo, accountEntity);
    }

    @Override
    public void delAccount(Long accountId) {
        accountDomainService.delAccount(accountId);
    }

    @Override
    public AccountInfo findAccountInfo(Long accountId) {
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountId);

        final AccountEntity accountEntity =
                originOptional.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        Optional<UserEntity> userOpt = userRepository.findById(accountEntity.getAccountOwnershipId());
        UserEntity userEntity = userOpt.orElseThrow(() -> new BizException(ResponseStatusEnum.USER_NONE_EXIST));

        AccountInfo accountInfo = AccountEntityConverter.accountEntity2AccountInfo(accountEntity);
        accountInfo.setAccountOwner(userEntity.getUserName());

        return accountInfo;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void transfer(AccountTransferInfo transferInfo) {

        Optional<AccountEntity> sourceAccountOpt = accountRepository.findById(transferInfo.getAccountId());
        final AccountEntity sourceAccount =
                sourceAccountOpt.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        // valid account balance
        long accountBalance = sourceAccount.getAccountBalance();
        BizAssert.isTrue(accountBalance > transferInfo.getTransferBalance(), ResponseStatusEnum.ACCOUNT_BALANCE_NOT_ENOUGH);

        // valid target account
        Optional<AccountEntity> targetAccountOpt = accountRepository.findById(transferInfo.getAccountId());
        final AccountEntity targetAccount =
                targetAccountOpt.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        // record transfer
        recordTransfer(transferInfo, sourceAccount, targetAccount);

        accountDomainService.transfer(transferInfo,sourceAccount,targetAccount);

    }

    private void recordTransfer(AccountTransferInfo transferInfo, AccountEntity sourceAccount, AccountEntity targetAccount) {
        FundsRecordInfo transferOut = new FundsRecordInfo();
        transferOut.setFundsUserId(sourceAccount.getAccountOwnershipId());
        transferOut.setFundsAccountId(transferInfo.getAccountId());
        transferOut.setFundsRecordTime(LocalDateTime.now());
        transferOut.setFundsRecordDescribe(TRANSFER_OUT.getClassifyName());
        transferOut.setFundsRecordClassifyId(TRANSFER_OUT.getClassifyId());
        transferOut.setFundsRecordRemark(String.format("从账户:{%s} 转出到 账户:{%s}", sourceAccount.getAccountName(),
                targetAccount.getAccountName()));
        transferOut.setFundsRecordBalance(transferInfo.getTransferBalance());

        fundsRecordDomainService.recordFunds(transferOut);

        FundsRecordInfo transferIn = new FundsRecordInfo();
        transferIn.setFundsUserId(sourceAccount.getAccountOwnershipId());
        transferIn.setFundsAccountId(transferInfo.getTargetAccountId());
        transferIn.setFundsRecordTime(LocalDateTime.now());
        transferIn.setFundsRecordDescribe(TRANSFER_IN.getClassifyName());
        transferIn.setFundsRecordClassifyId(TRANSFER_IN.getClassifyId());
        transferIn.setFundsRecordRemark(String.format("从账户:{%s} 转入到 账户:{%s}", targetAccount.getAccountName(),
                sourceAccount.getAccountName()));
        transferOut.setFundsRecordBalance(transferInfo.getTransferBalance());
        fundsRecordDomainService.recordFunds(transferIn);
    }
}
