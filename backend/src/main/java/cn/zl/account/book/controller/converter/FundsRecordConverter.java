package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.request.FundsRecordRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;

/**
 * @author lin.zl
 */
public final class FundsRecordConverter {

    public static FundsRecordResponseDTO fundsRecordInfo2FundsRecord(FundsRecordInfo fundsRecordInfo){

        FundsRecordResponseDTO resp = new FundsRecordResponseDTO();
        resp.setFundsRecordId(fundsRecordInfo.getFundsRecordId());
        resp.setFundsCode(fundsRecordInfo.getFundsCode());
        resp.setFundsRemark(fundsRecordInfo.getFundsRemark());
        resp.setFundsTime(fundsRecordInfo.getFundsTime());
        resp.setBalance(fundsRecordInfo.getBalance());
        resp.setAccountId(fundsRecordInfo.getAccountId());

        return resp;
    }

    public static FundsRecordInfo fundsRecordReq2FundsRecordInfo(FundsRecordRequestDTO fundsRecordReq){

        FundsRecordInfo info = new FundsRecordInfo();
        info.setFundsCode(fundsRecordReq.getFundsCode());
        info.setFundsRemark(fundsRecordReq.getFundsRemark());
        info.setFundsTime(fundsRecordReq.getFundsTime());
        info.setBalance(fundsRecordReq.getBalance());
        info.setAccountId(fundsRecordReq.getAccountId());

        return info;
    }


    private FundsRecordConverter() {
    }
}
