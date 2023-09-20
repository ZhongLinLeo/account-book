package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

/**
 * @author lin.zl
 */
public class NormalInstallmentCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        if (isRepayChange(calculateInfo.getCurrentRepayDate(), loanInfo)) {
            return LoanCalculateFactory.calculatePayInfo(loanInfo, calculateInfo, CalculateEnum.REPAY_CHANGE);
        }

        //  获取 LPR 信息
        double currentRate = calculateInfo.getCurrentRate();
        double totalAmount = calculateInfo.getRemainsPrincipal();
        Double repayAmount = calculateInfo.getRepayAmount();

        // 利息
        double interest = calculateInterest(totalAmount,currentRate);

        // 本金
        double principalPreMonth = calculatePrincipal(repayAmount,interest);

        return RepayAmountPreMonthInfo.builder()
                .repayAmount(repayAmount)
                .repayInterest(interest)
                .repayPrincipal(principalPreMonth)
                .remainsPrincipal(convert2Accuracy(totalAmount - principalPreMonth))
                .currentRate(currentRate)
                .build();
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
