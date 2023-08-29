package cn.zl.account.book.application.domain;

import cn.zl.account.book.application.info.AccountInfo;

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
     * @return true if success
     */
    void modifyAccount(AccountInfo accountInfo);

    /**
     * del account
     *
     * @param accountId account id
     * @return true if success
     */
    void delAccount(Long accountId);
}
