package cn.zl.account.book.domain;

import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;

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
     * @param classifyEntity classify info
     * @param fundsRecordInfo funds record info
     */
    void modifyFundRecord(FundsRecordEntity classifyEntity, FundsRecordInfo fundsRecordInfo);

    /**
     * del record
     *
     * @param recordEntity funds record info
     */
    void delFundRecord(FundsRecordEntity recordEntity);
}
