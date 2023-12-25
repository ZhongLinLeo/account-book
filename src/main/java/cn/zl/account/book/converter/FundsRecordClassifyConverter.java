package cn.zl.account.book.converter;

import cn.zl.account.book.info.FundsRecordClassifyInfo;
import cn.zl.account.book.enums.ClassifyTypeEnum;
import cn.zl.account.book.view.request.FundsRecordClassifyRequest;
import cn.zl.account.book.view.response.FundsRecordClassifyResponse;

/**
 * @author lin.zl
 */
public final class FundsRecordClassifyConverter {

    public static FundsRecordClassifyInfo req2Info(FundsRecordClassifyRequest fundsRecordClassifyReq) {
        return FundsRecordClassifyInfo.builder()
                .classifyName(fundsRecordClassifyReq.getClassifyName())
                .classifyType(fundsRecordClassifyReq.getClassifyType())
                .classifyDescribe(fundsRecordClassifyReq.getClassifyDescribe())
                .classifyIcon(fundsRecordClassifyReq.getClassifyIcon())
                .includeAnalyze(fundsRecordClassifyReq.getIncludeAnalyze())
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
                .classifyIcon(fundsRecordClassifyInfo.getClassifyIcon())
                .defaultClassify(fundsRecordClassifyInfo.getDefaultClassify())
                .includeAnalyze(fundsRecordClassifyInfo.getIncludeAnalyze())
                .build();
    }

    private FundsRecordClassifyConverter() {
    }
}
