package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.LoanLprInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * 利率变更，不会缩短期数，只会缩短每月还款金额
 *
 * @author lin.zl
 */
public class LprChangeCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {
        double totalAmount = calculateInfo.getRemainsPrincipal();
        int loanPeriod = calculateInfo.getLoanPeriod();
        LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();

        double currentRate = findCurrentRate(loanInfo,currentRepayDate);

        Double repayAmount = calculateInfo.getRepayAmount();
        if (Objects.isNull(repayAmount)) {
            repayAmount = repayAmountPreMonth(currentRate, totalAmount, loanPeriod);
            // 设置还款金额
            calculateInfo.setRepayAmount(repayAmount);
        }

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
                .build();
    }

    private double findCurrentRate(LoanInfo loanInfo, LocalDate currentRepayDate) {
        Optional<LoanLprInfo> lprInfo = loanInfo.getLoanLprInfos().stream().filter(e -> {
            LocalDate lprDate = e.getLprDate();
            LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
            return lprDate.isBefore(currentRepayDate) && lprDate.isAfter(lastRepayDate);
        }).findFirst();

        if (lprInfo.isPresent()){
            return lprInfo.get().getLpr();
        }

        throw new RuntimeException();
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.FIRST_INSTALLMENT;
    }
}
