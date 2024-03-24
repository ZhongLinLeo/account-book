package cn.zl.account.book.infrastructure.DO;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class AnalyzeTrendBo {

    /**
     * classify type
     */
    private Integer classifyType;

    /**
     * fundsRecordDate
     */
    private String fundsRecordDate;

    /**
     * totalFundsBalance
     */
    private Long totalFundsBalance;
}
