package cn.zl.account.book.infrastructure.bo;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
public interface AnalyzeTopsBo {

    /**
     * classify name
     *
     * @return ClassifyName
     */
    String getClassifyName();

    /**
     * balance
     *
     * @return FundsRecordBalance
     */
    Long getFundsRecordBalance();

    /**
     * desc
     *
     * @return FundsRecordDesc
     */
    String getFundsRecordDesc();

    /**
     * RecordTime
     *
     * @return FundsRecordTime
     */
    LocalDateTime getFundsRecordTime();

}
