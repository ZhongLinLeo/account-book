package cn.zl.account.book.controller.application;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;

import java.util.List;

/**
 * @author lin.zl
 */
public interface FundsRecordClassifyAppService {

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

    /**
     * list all
     *
     * @return classify infos
     */
    List<FundsRecordClassifyInfo> listClassify();

}
