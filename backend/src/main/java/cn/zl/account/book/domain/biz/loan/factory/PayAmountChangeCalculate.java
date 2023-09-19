package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountChangeInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @author lin.zl
 */
public class PayAmountChangeCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        double currentRate = calculateInfo.getCurrentRate();
        double totalAmount = calculateInfo.getRemainsPrincipal();
        int loanPeriod = calculateInfo.getLoanPeriod();

        double repayAmount = findRepayAmount(loanInfo, calculateInfo.getCurrentRepayDate());
        // 设置还款金额
        calculateInfo.setRepayAmount(repayAmount);

        int reduceMonths = reduceMonths(currentRate, totalAmount, loanPeriod, repayAmount);

        double principalPreMonth = calculatePrincipalPreMonth(currentRate, totalAmount, repayAmount, 1);
        // 每月还款利息 = 每月还款金额 - 每月还款本金
        double interest = convert2Double(BigDecimal.valueOf(repayAmount)
                .add(BigDecimal.valueOf(principalPreMonth).negate()));

        return RepayAmountPreMonthInfo.builder()
                .repayTimes(1)
                .repayDate(calculateInfo.getCurrentRepayDate())
                .repayAmount(repayAmount)
                .repayInterest(interest)
                .repayPrincipal(principalPreMonth)
                .remainsPrincipal(totalAmount  - principalPreMonth)
                .currentRate(currentRate)
                .reduceMonths(reduceMonths)
                .build();
    }

    private double findRepayAmount(LoanInfo loanInfo, LocalDate currentRepayDate) {
        Optional<RepayAmountChangeInfo> prepaymentInfo = loanInfo.getRepayAmountChangeInfos().stream().filter(e -> {
            LocalDate payAmountChange = e.getChangeDate();
            LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
            return payAmountChange.isBefore(currentRepayDate) && payAmountChange.isAfter(lastRepayDate);
        }).findFirst();

        if (prepaymentInfo.isPresent()) {
            return prepaymentInfo.get().getRepayAmount();
        }

        throw new RuntimeException();
    }

    private int reduceMonths(double rate, double originLoanAmount,int loanPeriod, double originPayAmount) {
        // 暴力破解
        int reduceMonths = 0;
        for (int period = loanPeriod; period > 0; period--) {
            double amountPreMonth = repayAmountPreMonth(rate, originLoanAmount, period);
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
