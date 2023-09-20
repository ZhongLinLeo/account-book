package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 抽象方法
 *
 * @author lin.zl
 */
public abstract class BaseLoanCalculate {

    /**
     * 还款计算
     *
     * @param loanInfo      loan info
     * @param calculateInfo 计算信息
     * @return 还款信息
     */
    public abstract RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo);

    /**
     * 计算类型
     *
     * @return 返回计算类型
     */
    public abstract CalculateEnum calculateType();

    /**
     * x = A * β * (1 + β) ^ k / [(1 + β) ^ k - 1]
     * 贷款总金额为A，月利率为β，贷款期数为k，每期需还款总金额（本金+利息）为x
     *
     * @param rate       年利率
     * @param loanAmount 剩余贷款总金额
     * @param loanPeriod 剩余期数
     * @return 每期还款金额
     */
    protected double repayAmountPreMonth(double rate, double loanAmount, int loanPeriod) {

        final BigDecimal monthRate = BigDecimal.valueOf(rate / 100 / 12);

        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(loanPeriod);

        final BigDecimal molecular = BigDecimal.valueOf(loanAmount).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 4, BigDecimal.ROUND_HALF_UP);
        return convert2Double(repayAmount);
    }


    public double convert2Double(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal monthRate(double currentRate) {
        return BigDecimal.valueOf(currentRate / 100 / 12);
    }


    /**
     * 计算 还款本金
     *
     * @param rate       年利率
     * @param loanAmount 贷款金额
     * @param payAmount  贷款期限
     * @param times      还款期数
     * @return 本金
     */
    protected double calculatePrincipalPreMonth(double rate, double loanAmount, double payAmount, int times) {
        final BigDecimal monthRate = monthRate(rate);

        final BigDecimal tmp = BigDecimal.valueOf(loanAmount).multiply(monthRate);

        final BigDecimal firstPrincipal = BigDecimal.valueOf(payAmount).add(tmp.negate());

        // Pn = P1 * (1 + β) ^ (n - 1)

        final BigDecimal principal = firstPrincipal.multiply(BigDecimal.ONE.add(monthRate).pow(times - 1));
        return convert2Double(principal);
    }

    protected static boolean isCurrentPeriod(LocalDate targetDate, LocalDate currentRepayDate) {
        LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
        return targetDate.compareTo(lastRepayDate) >= 0 && targetDate.compareTo(currentRepayDate) < 0;
    }


    protected boolean isRepayChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        return isRepayAmountChange(currentRepayDate, loanInfo) || isPrepay(currentRepayDate, loanInfo)
                || isLprChange(currentRepayDate, loanInfo);
    }

    protected boolean isRepayAmountChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return false;
        }

        return repayAmountChangeInfos.stream().anyMatch(e -> {
            LocalDate changeDate = e.getChangeDate();
            return isCurrentPeriod(changeDate, currentRepayDate);
        });
    }

    protected boolean isPrepay(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return false;
        }

        return prepaymentInfos.stream().anyMatch(e -> {
            LocalDate prepaymentDate = e.getPrepaymentDate();
            return isCurrentPeriod(prepaymentDate, currentRepayDate);
        });
    }

    protected boolean isLprChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos();
        if (CollectionUtils.isEmpty(loanLprInfos)) {
            return false;
        }

        return loanLprInfos.stream().anyMatch(e -> {
            LocalDate lprDate = e.getLprDate();
            return isCurrentPeriod(lprDate, currentRepayDate);
        });
    }

}
