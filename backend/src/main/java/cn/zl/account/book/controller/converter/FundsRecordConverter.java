package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.request.FundsRecordRequest;
import cn.zl.account.book.controller.response.FundsRecordResponse;

/**
 * @author lin.zl
 */
public final class FundsRecordConverter {

    public static FundsRecordResponse info2Resp(FundsRecordInfo fundsRecordInfo){

        FundsRecordResponse resp = new FundsRecordResponse();
        resp.setFundsAccountId(fundsRecordInfo.getFundsAccountId());
        resp.setFundsRecordRemark(fundsRecordInfo.getFundsRecordRemark());
        resp.setFundsRecordBalance(fundsRecordInfo.getFundsRecordBalance());
        resp.setFundsRecordClassifyId(fundsRecordInfo.getFundsRecordClassifyId());
        resp.setFundsRecordTime(fundsRecordInfo.getFundsRecordTime());
        resp.setFundsRecordDescribe(fundsRecordInfo.getFundsRecordDescribe());

        return resp;
    }

    public static FundsRecordInfo req2Info(FundsRecordRequest fundsRecordReq){
        FundsRecordInfo info = new FundsRecordInfo();
        info.setFundsRecordClassifyId(fundsRecordReq.getFundsRecordClassifyId());
        info.setFundsRecordTime(fundsRecordReq.getFundsRecordTime());

        return info;
    }

    private FundsRecordConverter() {
        // non
    }
}
