package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.request.FundsRecordClassifyRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordClassifyResponseDTO;

/**
 * @author lin.zl
 */
public final class FundsRecordClassifyConverter {

    public static FundsRecordClassifyInfo req2Info(FundsRecordClassifyRequestDTO fundsRecordClassifyReq) {
        return FundsRecordClassifyInfo.builder()
                .classifyName(fundsRecordClassifyReq.getClassifyName())
                .classifyType(fundsRecordClassifyReq.getClassifyType())
                .classifyDescribe(fundsRecordClassifyReq.getClassifyDescribe())
                .parentClassifyId(fundsRecordClassifyReq.getParentClassifyId())
                .build();
    }

    public static FundsRecordClassifyResponseDTO info2Resp(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        return FundsRecordClassifyResponseDTO.builder()
                .classifyId(fundsRecordClassifyInfo.getClassifyId())
                .classifyName(fundsRecordClassifyInfo.getClassifyName())
                .classifyType(fundsRecordClassifyInfo.getClassifyType())
                .classifyDescribe(fundsRecordClassifyInfo.getClassifyDescribe())
                .parentClassifyId(fundsRecordClassifyInfo.getParentClassifyId())
                .parentClassifyName(fundsRecordClassifyInfo.getParentClassifyName())
                .build();
    }

    private FundsRecordClassifyConverter() {
    }
}
