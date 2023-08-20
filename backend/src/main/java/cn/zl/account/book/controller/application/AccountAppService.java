package cn.zl.account.book.controller.application;

import cn.zl.account.book.application.info.AccountInfo;

import java.util.List;

/**
 * @author lin.zl
 */
public interface AccountAppService {

    /**
     * create account info
     *
     * @param accountInfo account info
     * @return true if success
     */
    Boolean createAccount(AccountInfo accountInfo);

    /**
     * list account info
     *
     * @return account infos
     */
    List<AccountInfo> listAccount();

    /**
     * modify account
     *
     * @param accountInfo account info
     * @return true if success
     */
    Boolean modifyAccount(AccountInfo accountInfo);

    /**
     * del account
     *
     * @param accountId account id
     * @return true if success
     */
    Boolean delAccount(Long accountId);
}
