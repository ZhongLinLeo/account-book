package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.controller.request.FundsRecordClassifyRequest;
import cn.zl.account.book.controller.response.FundsRecordClassifyResponse;

/**
 * @author lin.zl
 */
public final class FundsRecordClassifyConverter {

    public static FundsRecordClassifyInfo req2Info(FundsRecordClassifyRequest fundsRecordClassifyReq) {
        return FundsRecordClassifyInfo.builder()
                .classifyName(fundsRecordClassifyReq.getClassifyName())
                .classifyType(fundsRecordClassifyReq.getClassifyType())
                .classifyDescribe(fundsRecordClassifyReq.getClassifyDescribe())
                .build();
    }

    public static FundsRecordClassifyResponse info2Resp(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        Integer classifyType = fundsRecordClassifyInfo.getClassifyType();

        return FundsRecordClassifyResponse.builder()
                .classifyId(fundsRecordClassifyInfo.getClassifyId())
                .classifyName(fundsRecordClassifyInfo.getClassifyName())
                .classifyType(classifyType)
                .classifyTypeName(ClassifyTypeEnum.findClassifyTypeName(classifyType))
                .classifyDescribe(fundsRecordClassifyInfo.getClassifyDescribe())
                .createTime(fundsRecordClassifyInfo.getCreateTime())
                .build();
    }

    private FundsRecordClassifyConverter() {
    }
}
