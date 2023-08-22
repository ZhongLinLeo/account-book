package cn.zl.account.book.application.domain;

import cn.zl.account.book.application.info.FundsRecordInfo;

/**
 * @author lin.zl
 */
public interface FundsRecordDomainService {
    /**
     * record
     *
     * @param fundsRecordInfo funds record info
     */
    void recordFunds(FundsRecordInfo fundsRecordInfo);

    /**
     * modify record
     *
     * @param fundsRecordInfo funds record info
     */
    void modifyFundRecord(FundsRecordInfo fundsRecordInfo);

    /**
     * del record
     *
     * @param recordId funds record id
     */
    void delFundRecord(Long recordId);

}
