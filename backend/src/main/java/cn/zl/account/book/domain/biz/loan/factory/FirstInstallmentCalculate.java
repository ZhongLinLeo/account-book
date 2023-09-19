package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * @author lin.zl
 */
public class FirstInstallmentCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        // calculate interest
        double currentRate = calculateInfo.getCurrentRate();
        double totalAmount = calculateInfo.getRemainsPrincipal();
        int loanRepayDay = calculateInfo.getLoanRepayDay();
        LocalDate loanStartDate = calculateInfo.getLoanStartDate();
        int loanPeriod = calculateInfo.getLoanPeriod();

        // 利息
        double interest = interestCal(currentRate, totalAmount, loanStartDate, loanRepayDay);

        Double repayAmount = calculateInfo.getRepayAmount();
        if (Objects.isNull(repayAmount)) {
            repayAmount = repayAmountPreMonth(currentRate, totalAmount, loanPeriod);

            // 设置还款金额
            calculateInfo.setRepayAmount(repayAmount);
        }

        double principalPreMonth = calculatePrincipalPreMonth(currentRate, totalAmount, repayAmount, 1);

        return RepayAmountPreMonthInfo.builder()
                .repayDate(calculateInfo.getCurrentRepayDate())
                .repayAmount(principalPreMonth + interest)
                .repayInterest(interest)
                .repayPrincipal(principalPreMonth)
                .remainsPrincipal(totalAmount  - principalPreMonth)
                .currentRate(currentRate)
                .build();
    }

    private double interestCal(double rate, double totalAmount, LocalDate loanStartDate,
                               Integer loanRepayDay) {
        final LocalDate lastRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay);
        Period period = Period.between(loanStartDate, lastRepayDate);
        int interestDay = 30 + period.getDays();

        final BigDecimal firstPeriodInterest = BigDecimal.valueOf(totalAmount)
                .multiply(monthRate(rate))
                .multiply(BigDecimal.valueOf(interestDay))
                .divide(BigDecimal.valueOf(30), 4, BigDecimal.ROUND_HALF_UP);
        return convert2Double(firstPeriodInterest);
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
