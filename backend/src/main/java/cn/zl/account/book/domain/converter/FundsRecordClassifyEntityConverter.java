package cn.zl.account.book.domain.converter;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;

/**
 * @author lin.zl
 */
public final class FundsRecordClassifyEntityConverter {

    public static FundsRecordClassifyInfo entity2Info(FundsRecordClassifyEntity entity) {
        return FundsRecordClassifyInfo.builder()
                .classifyId(entity.getClassifyId())
                .classifyName(entity.getClassifyName())
                .classifyType(entity.getClassifyType())
                .classifyDescribe(entity.getClassifyDescribe())
                .createTime(entity.getCreateTime())
                .build();
    }

    public static FundsRecordClassifyEntity info2entity(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        FundsRecordClassifyEntity entity = new FundsRecordClassifyEntity();
        entity.setClassifyId(fundsRecordClassifyInfo.getClassifyId());
        entity.setClassifyName(fundsRecordClassifyInfo.getClassifyName());
        entity.setClassifyType(fundsRecordClassifyInfo.getClassifyType());
        entity.setClassifyDescribe(fundsRecordClassifyInfo.getClassifyDescribe());

        return entity;
    }


    private FundsRecordClassifyEntityConverter() {
    }
}
