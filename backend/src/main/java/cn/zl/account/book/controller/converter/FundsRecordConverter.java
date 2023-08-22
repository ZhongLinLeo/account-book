package cn.zl.account.book.controller.converter;

import cn.zl.account.book.controller.response.FundsRecordResponseDTO;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;

/**
 * @author lin.zl
 */
public final class FundsRecordConverter {

    public static FundsRecordResponseDTO fundsRecordEntity2FundsRecord(FundsRecordEntity entity){

        FundsRecordResponseDTO resp = new FundsRecordResponseDTO();
        resp.setFundsRecordId(entity.getFundsRecordId());
        resp.setFundsCode(entity.getFundsCode());
        resp.setFundsRemark(entity.getFundsRemark());
        resp.setFundsTime(entity.getFundsTime());
        resp.setBalance(entity.getBalance());
        resp.setAccountId(entity.getAccountId());

        return resp;
    }


    private FundsRecordConverter() {
    }
}
