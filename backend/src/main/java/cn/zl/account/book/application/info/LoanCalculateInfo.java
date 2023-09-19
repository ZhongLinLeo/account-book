package cn.zl.account.book.application.info;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author lin.zl
 */
@Data
@Builder
public class LoanCalculateInfo {

    private double currentRate;

    private double lastRate;

    private double remainsPrincipal;

    private int loanPeriod;

    private int loanRepayDay;

    private LocalDate currentRepayDate;

    private LocalDate loanStartDate;

    private Double repayAmount;
}
