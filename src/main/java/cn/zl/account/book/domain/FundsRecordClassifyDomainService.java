package cn.zl.account.book.domain;

import cn.zl.account.book.info.FundsRecordClassifyInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;

/**
 * @author lin.zl
 */
public interface FundsRecordClassifyDomainService {
    /**
     * add
     *
     * @param fundsRecordClassifyInfo classify info
     */
    void addClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo);

    /**
     * modify
     *
     * @param fundsRecordClassifyInfo classify info
     * @param classifyEntity original classify
     */
    void modifyClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo, FundsRecordClassifyEntity classifyEntity);

    /**
     * del
     *
     * @param classifyEntity classify
     */
    void delClassify(FundsRecordClassifyEntity classifyEntity);

}
