package cn.zl.account.book.controller.application;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.application.info.AccountTransferInfo;

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
    void createAccount(AccountInfo accountInfo);

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
    void modifyAccount(AccountInfo accountInfo);

    /**
     * del account
     *
     * @param accountId account id
     * @return true if success
     */
    void delAccount(Long accountId);

    /**
     * get account info
     *
     * @param accountId accountId
     * @return account infos
     */
    AccountInfo findAccountInfo(Long accountId);

    /**
     * transfer
     *
     * @param transferInfo transfer info
     */
    void transfer(AccountTransferInfo transferInfo);
}
