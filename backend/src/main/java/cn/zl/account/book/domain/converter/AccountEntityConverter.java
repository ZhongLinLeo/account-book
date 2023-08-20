package cn.zl.account.book.domain.converter;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.infrastructure.entity.AccountEntity;

/**
 * @author create by leo.zl on 2023/8/20
 */
public final class AccountEntityConverter {

    public static AccountInfo accountEntity2AccountInfo(AccountEntity accountEntity){
       return AccountInfo.builder()
                .accountId(accountEntity.getAccountId())
                .accountName(accountEntity.getAccountName())
                .accountDescribe(accountEntity.getAccountDescribe())
                .accountBalance(accountEntity.getAccountBalance())
                .accountIncome(accountEntity.getAccountIncome())
                .accountExpenditure(accountEntity.getAccountExpenditure())
                .build();
    }

    public static AccountEntity accountInfo2AccountEntity(AccountInfo accountInfo){
        final AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(accountInfo.getAccountId());
        accountEntity.setAccountName(accountInfo.getAccountName());
        accountEntity.setAccountDescribe(accountInfo.getAccountDescribe());
        accountEntity.setAccountBalance(accountInfo.getAccountBalance());
        accountEntity.setAccountIncome(accountInfo.getAccountIncome());
        accountEntity.setAccountExpenditure(accountEntity.getAccountExpenditure());

        return accountEntity;
    }




    private AccountEntityConverter() {
    }
}
