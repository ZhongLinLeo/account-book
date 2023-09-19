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
        //  获取 LPR 信息
        double currentRate = calculateInfo.getCurrentRate();

        // 获取 剩余待还金额
        double totalAmount = getRemainsPrincipal(calculateInfo, loanInfo);

        // 每月还款金额
        Double repayAmount = calculateInfo.getRepayAmount();

        if (isRepayChange(calculateInfo.getCurrentRepayDate(), loanInfo)) {
            //  获取 LPR 信息
            currentRate = getCurrentRate(calculateInfo, loanInfo);

            // 获取 剩余待还金额
            totalAmount = getRemainsPrincipal(calculateInfo, loanInfo);

            // 每月还款金额
            repayAmount = getRepayAmount(calculateInfo, loanInfo, currentRate, totalAmount);
        }

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

    private double getRepayAmount(LoanCalculateInfo calculateInfo, LoanInfo loanInfo
            , double currentRate, double totalAmount) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        double repayAmount = calculateInfo.getRepayAmount();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return repayAmount;
        }

        final LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        for (RepayAmountChangeInfo repayAmountChangeInfo : repayAmountChangeInfos) {
            LocalDate changeDate = repayAmountChangeInfo.getChangeDate();
            if (isCurrentPeriod(changeDate, currentRepayDate)) {
                repayAmount = repayAmountChangeInfo.getRepayAmount();
                calculateInfo.setRepayAmount(repayAmount);
            }
        }
        return repayAmount;
    }

    private double getRemainsPrincipal(LoanCalculateInfo calculateInfo, LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        double remainsPrincipal = calculateInfo.getRemainsPrincipal();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return remainsPrincipal;
        }

        final LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        for (PrepaymentInfo prepaymentInfo : prepaymentInfos) {
            LocalDate prepaymentDate = prepaymentInfo.getPrepaymentDate();
            if (isCurrentPeriod(prepaymentDate, currentRepayDate)) {
                remainsPrincipal -= prepaymentInfo.getPrepaymentAmount();
            }
        }
        return remainsPrincipal;
    }

    private double getCurrentRate(LoanCalculateInfo calculateInfo, LoanInfo loanInfo) {
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos();
        double currentRate = calculateInfo.getCurrentRate();
        if (CollectionUtils.isEmpty(loanLprInfos)) {
            return currentRate;
        }

        final LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        for (LoanLprInfo loanLprInfo : loanLprInfos) {
            LocalDate lprDate = loanLprInfo.getLprDate();
            if (isCurrentPeriod(lprDate, currentRepayDate)) {
                currentRate = loanLprInfo.getLpr();
                // 如果 lpr 改变，将新的LPR设置进去
                calculateInfo.setCurrentRate(currentRate);
            }
        }
        return currentRate;
    }

    private boolean isRepayChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        return isRepayAmountChange(currentRepayDate, loanInfo) || isPrepay(currentRepayDate, loanInfo)
                || isLprChange(currentRepayDate, loanInfo);
    }

    private boolean isRepayAmountChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return false;
        }

        return repayAmountChangeInfos.stream().anyMatch(e -> {
            LocalDate changeDate = e.getChangeDate();
            return isCurrentPeriod(changeDate, currentRepayDate);
        });
    }

    private boolean isPrepay(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return false;
        }

        return prepaymentInfos.stream().anyMatch(e -> {
            LocalDate prepaymentDate = e.getPrepaymentDate();
            return isCurrentPeriod(prepaymentDate, currentRepayDate);
        });
    }


    private boolean isLprChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
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
