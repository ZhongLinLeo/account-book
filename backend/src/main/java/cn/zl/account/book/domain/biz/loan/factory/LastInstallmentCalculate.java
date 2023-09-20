package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author lin.zl
 */
public class LastInstallmentCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        // calculate interest
        double currentRate = calculateInfo.getCurrentRate();
        double totalAmount = calculateInfo.getRemainsPrincipal();
        int loanPeriod = calculateInfo.getLoanPeriod();

        // 利息
        double interest = lastPeriodInterest(currentRate, totalAmount);

        Double repayAmount = calculateInfo.getRepayAmount();
        if (Objects.isNull(repayAmount)) {
            repayAmount = repayAmountPreMonth(currentRate, totalAmount, loanPeriod);
        }

        double principalPreMonth = calculatePrincipalPreMonth(currentRate, totalAmount, repayAmount, 1);

        return RepayAmountPreMonthInfo.builder()
                .repayAmount(repayAmount)
                .repayInterest(interest)
                .repayPrincipal(principalPreMonth)
                .remainsPrincipal(totalAmount  - principalPreMonth)
                .currentRate(currentRate)
                .build();
    }

    private double lastPeriodInterest(double currentRate, double amount) {
        final BigDecimal lastPeriodInterest = BigDecimal.valueOf(amount)
                .multiply(monthRate(currentRate));
        return convert2Double(lastPeriodInterest);
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
