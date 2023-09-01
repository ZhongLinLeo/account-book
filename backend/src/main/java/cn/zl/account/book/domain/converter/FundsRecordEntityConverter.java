package cn.zl.account.book.domain.converter;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;

/**
 * @author lin.zl
 */
public final class FundsRecordEntityConverter {

    public static FundsRecordInfo entity2Info(FundsRecordEntity entity) {
        FundsRecordInfo info = new FundsRecordInfo();
        info.setFundsRecordId(entity.getFundsRecordId());
        info.setFundsRecordClassifyId(entity.getFundsRecordClassifyId());
        info.setFundsRecordDescribe(entity.getFundsRecordDescribe());
        info.setFundsRecordRemark(entity.getFundsRecordRemark());
        info.setFundsRecordBalance(entity.getFundsRecordBalance());
        info.setFundsAccountId(entity.getFundsAccountId());
        return info;
    }

    public static FundsRecordEntity info2Entity(FundsRecordInfo info){
        FundsRecordEntity entity = new FundsRecordEntity();
        entity.setFundsRecordId(info.getFundsRecordId());
        entity.setFundsRecordClassifyId(info.getFundsRecordClassifyId());
        entity.setFundsRecordRemark(info.getFundsRecordRemark());
        entity.setFundsRecordTime(info.getFundsRecordTime());
        entity.setFundsRecordBalance(info.getFundsRecordBalance());
        entity.setFundsAccountId(info.getFundsAccountId());
        return entity;
    }


    private FundsRecordEntityConverter() {
    }
}
