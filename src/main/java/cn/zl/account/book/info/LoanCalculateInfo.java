package cn.zl.account.book.info;

import lombok.Data;

import java.time.LocalDate;

/**
 * 计算信息，是会变更的信息
 *
 * @author lin.zl
 */
@Data
public class LoanCalculateInfo {

    /**
     * 上一期利率
     */
    private double currentRate;

    /**
     * 剩余金额
     */
    private double remainsPrincipal;

    /**
     * 剩余期数
     */
    private int remainsPeriod;

    /**
     * 还款日
     */
    private int loanRepayDay;

    /**
     * 当前期还款时间
     */
    private LocalDate currentRepayDate;

    /**
     * 贷款开始日期
     */
    private LocalDate loanStartDate;

    /**
     * 上一期还款金额
     */
    private Double repayAmount;
}
