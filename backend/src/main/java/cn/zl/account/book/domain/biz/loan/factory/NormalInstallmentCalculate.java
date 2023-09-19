package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
                .remainsPrincipal(totalAmount - principalPreMonth)
                .currentRate(currentRate)
                .build();
    }


    private void repayChange() {


    }

    private static boolean isRepayChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return false;
        }

        return repayAmountChangeInfos.stream().anyMatch(e -> {
            LocalDate changeDate = e.getChangeDate();
            return isCurrentPeriod(changeDate,currentRepayDate);
        });
    }

    private static boolean isPrepay(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return false;
        }

        return prepaymentInfos.stream().anyMatch(e -> {
            LocalDate prepaymentDate = e.getPrepaymentDate();
            return isCurrentPeriod(prepaymentDate, currentRepayDate);
        });
    }

    private static boolean isLprChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos();
        if (CollectionUtils.isEmpty(loanLprInfos)) {
            return false;
        }

        return loanLprInfos.stream().anyMatch(e -> {
            LocalDate lprDate = e.getLprDate();
            return isCurrentPeriod(lprDate, currentRepayDate);
        });
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
