package cn.zl.account.book.domain.converter;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;

/**
 * @author lin.zl
 */
public final class FundsRecordEntityConverter {

    public static FundsRecordInfo entity2Info(FundsRecordEntity entity){
        FundsRecordInfo info = new FundsRecordInfo();
        info.setFundsRecordId(entity.getFundsRecordId());
        info.setFundsCode(entity.getFundsCode());
        info.setFundsRemark(entity.getFundsRemark());
        info.setFundsTime(entity.getFundsTime());
        info.setBalance(entity.getBalance());
        info.setAccountId(entity.getAccountId());
        return info;
    }

    public static FundsRecordEntity info2Entity(FundsRecordInfo info){
        FundsRecordEntity entity = new FundsRecordEntity();
        entity.setFundsRecordId(info.getFundsRecordId());
        entity.setFundsCode(info.getFundsCode());
        entity.setFundsRemark(info.getFundsRemark());
        entity.setFundsTime(info.getFundsTime());
        entity.setBalance(info.getBalance());
        entity.setAccountId(info.getAccountId());
        return entity;
    }


    private FundsRecordEntityConverter() {
    }
}
