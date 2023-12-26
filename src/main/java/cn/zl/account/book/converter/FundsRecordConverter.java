package cn.zl.account.book.converter;

import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.info.FundsRecordSearchInfo;
import cn.zl.account.book.util.RmbUtils;
import cn.zl.account.book.view.request.FundsRecordQueryRequest;
import cn.zl.account.book.view.request.FundsRecordRequest;
import cn.zl.account.book.view.response.FundsRecordResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author lin.zl
 */
public final class FundsRecordConverter {

    public static FundsRecordResponse info2Resp(FundsRecordInfo fundsRecordInfo) {

        FundsRecordResponse resp = new FundsRecordResponse();
        resp.setFundsRecordId(fundsRecordInfo.getFundsRecordId());
        resp.setFundsRecordBalance(RmbUtils.convertFen2Yuan(fundsRecordInfo.getFundsRecordBalance()));
        resp.setFundsRecordTime(fundsRecordInfo.getFundsRecordTime());
        resp.setFundsRecordDescribe(fundsRecordInfo.getFundsRecordDescribe());
        resp.setFundsRecordRemark(fundsRecordInfo.getFundsRecordRemark());
        resp.setFundsUserId(fundsRecordInfo.getFundsUserId());

        return resp;
    }

    public static FundsRecordInfo req2Info(FundsRecordRequest fundsRecordReq) {
        FundsRecordInfo info = new FundsRecordInfo();
        info.setFundsRecordBalance(RmbUtils.convertYuan2Fen(fundsRecordReq.getFundsRecordBalance()));
        info.setFundsRecordDescribe(fundsRecordReq.getFundsRecordDescribe());
        info.setFundsRecordTime(fundsRecordReq.getFundsRecordTime());
        info.setFundsRecordRemark(fundsRecordReq.getFundsRecordRemark());
        info.setFundsRecordClassifyId(fundsRecordReq.getFundsRecordClassifyId());
        info.setFundsAccountId(fundsRecordReq.getFundsRecordAccountId());
        info.setFundsUserId(fundsRecordReq.getFundsUserId());

        return info;
    }

    public static FundsRecordSearchInfo req2Info(FundsRecordQueryRequest recordQueryRequest) {
        final LocalDate startTime = recordQueryRequest.getStartTime();
        final LocalDate endTime = recordQueryRequest.getEndTime();

        return FundsRecordSearchInfo.builder()
                .startTime(Objects.isNull(startTime) ? null : LocalDateTime.of(startTime, LocalTime.MIN))
                .endTime(Objects.isNull(endTime) ? null : LocalDateTime.of(endTime, LocalTime.MAX))
                .classifyIds(recordQueryRequest.getClassifyIds())
                .accountIds(recordQueryRequest.getAccountIds())
                .recordKeyword(recordQueryRequest.getRecordKeyword())
                .build();
    }

    private FundsRecordConverter() {
        // non
    }
}
