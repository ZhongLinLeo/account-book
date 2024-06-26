package cn.zl.account.book.application;

import cn.zl.account.book.info.AccountInfo;
import cn.zl.account.book.info.AccountTransferInfo;
import cn.zl.account.book.enums.ClassifyTypeEnum;

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

    void fundsRecord(Long accountId, Long balance, ClassifyTypeEnum classifyType);

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

    /**
     * repayment
     *
     * @param repaymentReq repayment req
     */
    void repayment(AccountTransferInfo repaymentReq);
}
