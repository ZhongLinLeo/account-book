package cn.zl.account.book.application.impl;

import cn.zl.account.book.domain.AccountDomainService;
import cn.zl.account.book.domain.FundsRecordDomainService;
import cn.zl.account.book.enums.AccountTypeEnum;
import cn.zl.account.book.info.AccountInfo;
import cn.zl.account.book.info.AccountTransferInfo;
import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.application.AccountAppService;
import cn.zl.account.book.enums.ClassifyTypeEnum;
import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.domain.converter.AccountEntityConverter;
import cn.zl.account.book.architecture.BizAssert;
import cn.zl.account.book.architecture.BizException;
import cn.zl.account.book.infrastructure.repository.AccountRepository;
import cn.zl.account.book.infrastructure.repository.UserRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.zl.account.book.enums.DefaultClassifyEnum.*;

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
    public void fundsRecord(Long accountId, Long balance, ClassifyTypeEnum classifyType) {
        final Optional<AccountEntity> originOptional = accountRepository.findById(accountId);

        final AccountEntity accountEntity =
                originOptional.orElseThrow(() -> new BizException(ResponseStatusEnum.FUNDS_RECORD_NONE_EXIST));

        long accountBalance = accountEntity.getAccountBalance();
        AccountInfo.AccountInfoBuilder builder = AccountInfo.builder()
                .accountBalance(accountBalance);

        if (Objects.equals(classifyType, ClassifyTypeEnum.EXPENDITURE)) {
            BizAssert.isTrue(isEnoughBalance(balance, accountEntity, accountBalance),
                    ResponseStatusEnum.ACCOUNT_BALANCE_NOT_ENOUGH);
            accountBalance -= balance;
            builder.accountExpenditure(accountEntity.getAccountExpenditure() + balance);
        }

        if (Objects.equals(classifyType, ClassifyTypeEnum.INCOME)) {
            accountBalance += balance;
            builder.accountIncome(accountEntity.getAccountIncome() + balance);
        }

        AccountInfo accountInfo = builder.accountBalance(accountBalance)
                .build();
        accountDomainService.modifyAccount(accountInfo, accountEntity);
    }

    private boolean isEnoughBalance(Long balance, AccountEntity accountEntity, long accountBalance) {
        return Objects.equals(accountEntity.getAccountType(),
                AccountTypeEnum.DEBIT_ACCOUNT.getAccountType()) && balance > accountBalance;
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
        BizAssert.isFalse(accountBalance > transferInfo.getTransferBalance(), ResponseStatusEnum.ACCOUNT_BALANCE_NOT_ENOUGH);

        // valid target account
        Optional<AccountEntity> targetAccountOpt = accountRepository.findById(transferInfo.getTargetAccountId());
        final AccountEntity targetAccount =
                targetAccountOpt.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        // record transfer
        recordTransfer(transferInfo, sourceAccount, targetAccount);

        accountDomainService.transfer(transferInfo, sourceAccount, targetAccount);

    }

    @Override
    public void repayment(AccountTransferInfo repaymentInfo) {

        Optional<AccountEntity> sourceAccountOpt = accountRepository.findById(repaymentInfo.getSourceAccountId());
        final AccountEntity sourceAccount =
                sourceAccountOpt.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));
        // valid account balance
        long accountBalance = sourceAccount.getAccountBalance();
        BizAssert.isTrue(accountBalance > repaymentInfo.getTransferBalance(), ResponseStatusEnum.ACCOUNT_BALANCE_NOT_ENOUGH);

        // valid target account
        Optional<AccountEntity> targetAccountOpt = accountRepository.findById(repaymentInfo.getAccountId());
        final AccountEntity targetAccount =
                targetAccountOpt.orElseThrow(() -> new BizException(ResponseStatusEnum.ACCOUNT_NONE_EXIST));

        // record transfer
        recordRepayment(repaymentInfo, sourceAccount, targetAccount);

        accountDomainService.repayment(repaymentInfo, sourceAccount, targetAccount);
    }

    private void recordRepayment(AccountTransferInfo repaymentInfo, AccountEntity sourceAccount, AccountEntity targetAccount) {
        FundsRecordInfo repaymentOut = new FundsRecordInfo();
        repaymentOut.setFundsUserId(sourceAccount.getAccountOwnershipId());
        repaymentOut.setFundsAccountId(sourceAccount.getAccountId());
        repaymentOut.setFundsRecordTime(repaymentInfo.getOperateTime());
        repaymentOut.setFundsRecordDescribe(REPAYMENT_OUT.getClassifyName());
        repaymentOut.setFundsRecordClassifyId(REPAYMENT_OUT.getClassifyId());
        repaymentOut.setFundsRecordRemark(String.format("还款，从账户:{%s} 转出到 账户:{%s}", sourceAccount.getAccountName(),
                targetAccount.getAccountName()));
        repaymentOut.setFundsRecordBalance(repaymentInfo.getTransferBalance());
        fundsRecordDomainService.recordFunds(repaymentOut);

        FundsRecordInfo transferIn = new FundsRecordInfo();
        transferIn.setFundsUserId(sourceAccount.getAccountOwnershipId());
        transferIn.setFundsAccountId(targetAccount.getAccountId());
        transferIn.setFundsRecordTime(repaymentInfo.getOperateTime());
        transferIn.setFundsRecordDescribe(REPAYMENT_IN.getClassifyName());
        transferIn.setFundsRecordClassifyId(REPAYMENT_IN.getClassifyId());
        transferIn.setFundsRecordRemark(String.format("还款，从账户:{%s} 转入到 账户:{%s}", targetAccount.getAccountName(),
                sourceAccount.getAccountName()));
        repaymentOut.setFundsRecordBalance(repaymentInfo.getTransferBalance());
        fundsRecordDomainService.recordFunds(transferIn);
    }

    private void recordTransfer(AccountTransferInfo transferInfo, AccountEntity sourceAccount, AccountEntity targetAccount) {
        FundsRecordInfo transferOut = new FundsRecordInfo();
        transferOut.setFundsUserId(sourceAccount.getAccountOwnershipId());
        transferOut.setFundsAccountId(sourceAccount.getAccountId());
        transferOut.setFundsRecordTime(transferInfo.getOperateTime());
        transferOut.setFundsRecordDescribe(TRANSFER_OUT.getClassifyName());
        transferOut.setFundsRecordClassifyId(TRANSFER_OUT.getClassifyId());
        transferOut.setFundsRecordRemark(String.format("从账户:{%s} 转出到 账户:{%s}", sourceAccount.getAccountName(),
                targetAccount.getAccountName()));
        transferOut.setFundsRecordBalance(transferInfo.getTransferBalance());

        fundsRecordDomainService.recordFunds(transferOut);

        FundsRecordInfo transferIn = new FundsRecordInfo();
        transferIn.setFundsUserId(sourceAccount.getAccountOwnershipId());
        transferIn.setFundsAccountId(targetAccount.getAccountId());
        transferIn.setFundsRecordTime(transferInfo.getOperateTime());
        transferIn.setFundsRecordDescribe(TRANSFER_IN.getClassifyName());
        transferIn.setFundsRecordClassifyId(TRANSFER_IN.getClassifyId());
        transferIn.setFundsRecordRemark(String.format("从账户:{%s} 转入到 账户:{%s}", targetAccount.getAccountName(),
                sourceAccount.getAccountName()));
        transferIn.setFundsRecordBalance(transferInfo.getTransferBalance());
        fundsRecordDomainService.recordFunds(transferIn);
    }
}
