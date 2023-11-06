package cn.zl.account.book.application.domain;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.application.info.AccountTransferInfo;
import cn.zl.account.book.infrastructure.entity.AccountEntity;

import java.util.List;

/**
 * @author lin.zl
 */
public interface AccountDomainService {

    /**
     * list accounts
     *
     * @return list of account
     */
    List<AccountInfo> listAccounts();

    /**
     * create account info
     *
     * @param accountInfo account info
     * @return true if success
     */
    void createAccount(AccountInfo accountInfo);

    /**
     * modify account
     *
     * @param accountInfo account info
     * @param accountEntity entity
     * @return true if success
     */
    void modifyAccount(AccountInfo accountInfo, AccountEntity accountEntity);

    /**
     * del account
     *
     * @param accountId account id
     * @return true if success
     */
    void delAccount(Long accountId);

    /**
     * transfer
     *
     * @param transferInfo transfer info
     * @param sourceAccount source account
     * @param targetAccount target account
     */
    void transfer(AccountTransferInfo transferInfo, AccountEntity sourceAccount, AccountEntity targetAccount);

}
