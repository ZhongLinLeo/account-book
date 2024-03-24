package cn.zl.account.book.infrastructure.DO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class AnalyzeTopsBo {
//
//    /**
//     * classify name
//     *
//     * @return ClassifyName
//     */
//    String getClassifyName();
//
//    /**
//     * balance
//     *
//     * @return FundsRecordBalance
//     */
//    Long getFundsRecordBalance();
//
//    /**
//     * desc
//     *
//     * @return FundsRecordDesc
//     */
//    String getFundsRecordDesc();
//
//    /**
//     * RecordTime
//     *
//     * @return FundsRecordTime
//     */
//    LocalDateTime getFundsRecordTime();

    private String classifyName;
    private Long fundsRecordBalance;
    private String fundsRecordDesc;
    private LocalDateTime fundsRecordTime;
}
