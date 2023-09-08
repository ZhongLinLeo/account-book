package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.LoanLprInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;
import cn.zl.account.book.application.info.RepayInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * loan calculate
 *
 * @author lin.zl
 */
public class LoanDomain {


    public void calculatePrepayment(LoanInfo loanInfo) {
        // calculate Interest
        final List<RepayAmountPreMonthInfo> originRepayInfos = calculateRepayInfo(loanInfo);

        RepayInfo repayInfo = new RepayInfo();

        repayInfo.setLoanAmount(loanInfo.getLoanAmount());
        repayInfo.setInstallmentsNumber(loanInfo.getLoanPeriod());

        LocalDate now = LocalDate.now();
        List<RepayAmountPreMonthInfo> repaidList = originRepayInfos.stream()
                .filter(repayAmount -> repayAmount.getRepayDate().isBefore(now)).collect(Collectors.toList());

        repayInfo.setRepaidNumber(repaidList.size());
        repayInfo.setRepaidInterest(repaidList.stream().mapToDouble(RepayAmountPreMonthInfo::getRepayInterest).sum());
        double repaidPrincipal = repaidList.stream().mapToDouble(RepayAmountPreMonthInfo::getRepayPrincipal).sum();
        repayInfo.setRepaidPrincipal(repaidPrincipal);
        repayInfo.setRemainsPrincipal(BigDecimal.valueOf(loanInfo.getLoanAmount())
                .add(BigDecimal.valueOf(repaidPrincipal).negate()).doubleValue());

        repayInfo.setTotalInterest(originRepayInfos.stream()
                .mapToDouble(RepayAmountPreMonthInfo::getRepayInterest).sum());

        System.out.println(repayInfo);
    }

    private List<RepayAmountPreMonthInfo> calculateRepayInfo(LoanInfo loanInfo) {
        ArrayList<RepayAmountPreMonthInfo> repayAmountPreMonthInfos = new ArrayList<>();

        Map<LocalDate, Double> rateMap = loanInfo.getLoanLprInfos().stream()
                .collect(Collectors.toMap(LoanLprInfo::getLprDate, LoanLprInfo::getLpr));

        LocalDate loanStartDate = loanInfo.getLoanStartDate();
        Integer loanRepayDay = loanInfo.getLoanRepayDay();
        Double loanAmount = loanInfo.getLoanAmount();

        double currentRate = 5.1;
        Integer loanPeriod = loanInfo.getLoanPeriod();

        double remainsPrincipal = loanAmount;
        for (int times = 1; times <= loanPeriod; times++) {
            LocalDate currentRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay)
                    .plusMonths(times);
            currentRate = Optional.ofNullable(rateMap.get(currentRepayDate)).orElse(currentRate);
            RepayAmountPreMonthInfo repayAmountPreMonthInfo = new RepayAmountPreMonthInfo();
            repayAmountPreMonthInfo.setRepayDate(currentRepayDate);
            repayAmountPreMonthInfo.setRepayTimes(times);
            final double principalPreMonth = calculatePrincipalPreMonth(currentRate, loanAmount, loanPeriod, times);
            repayAmountPreMonthInfo.setRepayPrincipal(principalPreMonth);

            remainsPrincipal =
                    convert2Double(BigDecimal.valueOf(remainsPrincipal).add(BigDecimal.valueOf(principalPreMonth).negate()));
            repayAmountPreMonthInfo.setRemainsPrincipal(remainsPrincipal);

            repayAmountPreMonthInfos.add(repayAmountPreMonthInfo);

            // 第一期利息需要特殊计算
            if (times == 1) {
                final double firstPeriodInterest = firstPeriodInterest(currentRate, loanAmount, loanStartDate,
                        loanRepayDay);
                repayAmountPreMonthInfo.setRepayInterest(firstPeriodInterest);
                repayAmountPreMonthInfo.setRepayAmount(principalPreMonth + firstPeriodInterest);
                continue;
            }

            final double amountPreMonth = calculateRepayAmountPreMonth(currentRate, loanAmount, loanPeriod);
            repayAmountPreMonthInfo.setRepayAmount(amountPreMonth);

            // 每月还款利息 = 每月还款金额 - 每月还款本金
            repayAmountPreMonthInfo.setRepayInterest(convert2Double(BigDecimal.valueOf(amountPreMonth)
                    .add(BigDecimal.valueOf(principalPreMonth).negate())));
        }

        return repayAmountPreMonthInfos;
    }

    /**
     * x = A * β * (1 + β) ^ k / [(1 + β) ^ k - 1]
     * 贷款总金额为A，月利率为β，贷款期数为k，每期需还款总金额（本金+利息）为x
     *
     * @param rate       年利率
     * @param loanAmount 剩余贷款总金额
     * @param loanPeriod 剩余期数
     * @return 每期还款金额
     */
    private double calculateRepayAmountPreMonth(double rate, double loanAmount, int loanPeriod) {

        final BigDecimal monthRate = monthRate(rate);

        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(loanPeriod);

        final BigDecimal molecular = BigDecimal.valueOf(loanAmount).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 4, BigDecimal.ROUND_HALF_UP);
        return convert2Double(repayAmount);
    }

    private double calculatePrincipalPreMonth(double rate, double remainingPrincipal, int loanPeriod, int times) {
        final BigDecimal monthRate = monthRate(rate);

        final double amountPreMonth = calculateRepayAmountPreMonth(rate, remainingPrincipal, loanPeriod);

        final BigDecimal tmp = BigDecimal.valueOf(remainingPrincipal).multiply(monthRate);

        final BigDecimal firstPrincipal = BigDecimal.valueOf(amountPreMonth).add(tmp.negate());

        // Pn = P1 * (1 + β) ^ (n - 1)

        final BigDecimal principal = firstPrincipal.multiply(BigDecimal.ONE.add(monthRate).pow(times - 1));
        return convert2Double(principal);
    }

    private double calculateRepayPrincipalPreMonth(double rate, double totalAmount, Integer loanRepayDay) {


        return 1;
    }

    private double firstPeriodInterest(double rate, double totalAmount, LocalDate loanStartDate,
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


    private void payAmountPreMonth(double rate, double totalAmount) {

    }

    private static double convert2Double(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private BigDecimal convert2Approximate(BigDecimal value) {
        return value.divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal repayAmountWithDays(double currentRate, BigDecimal remainingPrincipal, Integer days) {
        final BigDecimal lprDecimal = BigDecimal.valueOf(currentRate);
        BigDecimal dayRate = lprDecimal.divide(BigDecimal.valueOf(360), 5, BigDecimal.ROUND_HALF_UP);
        return remainingPrincipal.multiply(dayRate).multiply(BigDecimal.valueOf(days));
    }

    private BigDecimal calculateMonthInterest(double currentRate, BigDecimal remainingPrincipal) {
        final BigDecimal monthRate = monthRate(currentRate);
        return remainingPrincipal.multiply(monthRate);
    }

    private static BigDecimal monthRate(double currentRate) {
        final BigDecimal lprDecimal = BigDecimal.valueOf(currentRate);
        return lprDecimal.divide(BigDecimal.valueOf(12), 5, BigDecimal.ROUND_HALF_UP);
    }


}
