package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.request.FundsRecordRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;

/**
 * @author lin.zl
 */
public final class FundsRecordConverter {

    public static FundsRecordResponseDTO info2Resp(FundsRecordInfo fundsRecordInfo){

        FundsRecordResponseDTO resp = new FundsRecordResponseDTO();
        resp.setFundsAccountId(fundsRecordInfo.getFundsAccountId());
        resp.setFundsRecordRemark(fundsRecordInfo.getFundsRecordRemark());
        resp.setFundsRecordBalance(fundsRecordInfo.getFundsRecordBalance());
        resp.setFundsRecordClassifyId(fundsRecordInfo.getFundsRecordClassifyId());
        resp.setFundsRecordTime(fundsRecordInfo.getFundsRecordTime());
        resp.setFundsRecordDescribe(fundsRecordInfo.getFundsRecordDescribe());

        return resp;
    }

    public static FundsRecordInfo req2Info(FundsRecordRequestDTO fundsRecordReq){
        FundsRecordInfo info = new FundsRecordInfo();
        info.setFundsRecordClassifyId(fundsRecordReq.getFundsRecordClassifyId());
        info.setFundsRecordTime(fundsRecordReq.getFundsRecordTime());

        return info;
    }

    private FundsRecordConverter() {
        // non
    }
}
