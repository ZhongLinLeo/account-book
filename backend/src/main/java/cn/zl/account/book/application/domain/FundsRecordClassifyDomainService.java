package cn.zl.account.book.application.domain;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;

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
     */
    void modifyClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo);

    /**
     * del
     *
     * @param classifyId classify id
     */
    void delClassify(Long classifyId);

}
