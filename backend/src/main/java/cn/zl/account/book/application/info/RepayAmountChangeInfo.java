package cn.zl.account.book.application.info;

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
}
