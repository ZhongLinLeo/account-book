package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.PrepaymentInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * 提前还款
 *
 * @author lin.zl
 */
public class PrepayChangeCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        double currentRate = calculateInfo.getCurrentRate();
        double totalAmount = calculateInfo.getRemainsPrincipal();
        int loanPeriod = calculateInfo.getLoanPeriod();

        Double repayAmount = calculateInfo.getRepayAmount();

        LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        double prepayAmount = findPrepayAmount(loanInfo, currentRepayDate);
        int reduceMonths = reduceMonths(currentRate, totalAmount, prepayAmount, loanPeriod, repayAmount);

        repayAmount = repayAmountPreMonth(currentRate, totalAmount, loanPeriod - reduceMonths);
        // 设置还款金额
        calculateInfo.setRepayAmount(repayAmount);

        double principalPreMonth = calculatePrincipalPreMonth(currentRate, totalAmount, repayAmount, 1);

        // 每月还款利息 = 每月还款金额 - 每月还款本金
        double interest = convert2Double(BigDecimal.valueOf(repayAmount)
                .add(BigDecimal.valueOf(principalPreMonth).negate()));

        return RepayAmountPreMonthInfo.builder()
                .repayTimes(1)
                .repayDate(currentRepayDate)
                .repayAmount(repayAmount)
                .repayInterest(interest)
                .repayPrincipal(principalPreMonth)
                .currentRate(currentRate)
                .reduceMonths(reduceMonths)
                .build();
    }

    private double findPrepayAmount(LoanInfo loanInfo, LocalDate currentRepayDate) {
        Optional<PrepaymentInfo> prepaymentInfo = loanInfo.getPrepaymentInfos().stream().filter(e -> {
            LocalDate prepaymentDate = e.getPrepaymentDate();
            LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
            return prepaymentDate.isBefore(currentRepayDate) && prepaymentDate.isAfter(lastRepayDate);
        }).findFirst();

        if (prepaymentInfo.isPresent()) {
            return prepaymentInfo.get().getPrepaymentAmount();
        }

        throw new RuntimeException();
    }

    private int reduceMonths(double rate, double originLoanAmount, double prepayAmount, int loanPeriod, double originPayAmount) {
        if (prepayAmount >= originLoanAmount) {
            return loanPeriod;
        }

        // 暴力破解
        double loanAmount = originLoanAmount - prepayAmount;
        int reduceMonths = 0;
        for (int period = loanPeriod; period > 0; period--) {
            double amountPreMonth = repayAmountPreMonth(rate, loanAmount, period);
            if (amountPreMonth > originPayAmount) {
                reduceMonths = period + 1;
            }
        }

        // 杭州银行只能缩短整年的
        return reduceMonths / 12 * 12;
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
