package cn.zl.account.book.domain.converter;

import cn.zl.account.book.info.FundsRecordClassifyInfo;
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
                .classifyIcon(entity.getClassifyIcon())
                .defaultClassify(entity.getDefaultClassify())
                .includeAnalyze(entity.getIncludeAnalyze())
                .build();
    }

    public static FundsRecordClassifyEntity info2entity(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        FundsRecordClassifyEntity entity = new FundsRecordClassifyEntity();
        entity.setClassifyId(fundsRecordClassifyInfo.getClassifyId());
        entity.setClassifyName(fundsRecordClassifyInfo.getClassifyName());
        entity.setClassifyType(fundsRecordClassifyInfo.getClassifyType());
        entity.setClassifyDescribe(fundsRecordClassifyInfo.getClassifyDescribe());
        entity.setClassifyIcon(fundsRecordClassifyInfo.getClassifyIcon());
        entity.setIncludeAnalyze(fundsRecordClassifyInfo.getIncludeAnalyze());

        return entity;
    }


    private FundsRecordClassifyEntityConverter() {
    }
}
