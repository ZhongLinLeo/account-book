package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.math.BigDecimal;

/**
 * @author lin.zl
 */
public class NormalInstallmentCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        double currentRate = calculateInfo.getCurrentRate();
        double totalAmount = calculateInfo.getRemainsPrincipal();

        Double repayAmount = calculateInfo.getRepayAmount();
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
                .build();
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
