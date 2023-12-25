package cn.zl.account.book.info;

import lombok.Data;

import java.time.LocalDate;

/**
 * 还款金额变更
 *
 * @author lin.zl
 */
@Data
public class RepayAmountChangeInfo {

    /**
     * 还款金额变更
     */
    private Double repayAmount;

    /**
     * 更改日期
     */
    private LocalDate changeDate;

    private Integer reduceMonths;
}
