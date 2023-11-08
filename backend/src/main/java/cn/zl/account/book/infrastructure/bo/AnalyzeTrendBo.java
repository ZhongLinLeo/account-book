package cn.zl.account.book.infrastructure.bo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author lin.zl
 */
@Data
public class AnalyzeTrendBo {

    private Integer classifyType;

    private LocalDate fundsRecordDate;

    private Long totalFundsBalance;

}
