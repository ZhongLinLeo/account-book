package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

/**
 * loan calculate
 * <p>
 * 1. 只有利率、金额有变更的时候，才会重新计算还款金额
 * 2. 杭州银行只能减少整年的
 * <p>
 * <p>
 * 想了解的信息
 * 1、每月还款金额 -- done
 * 2、已还金额、已还本金、已还利息、已还期数、剩余期数，这个可以通过计算已还信息得来
 * 3、利率、本金、还款金额变更后 每月还款信息 -- 变更后重新计算还款金额
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

    public void calculatePrepaymentInfo(LoanInfo loanInfo) {
        // 贷款金额是不变的
        Double loanAmount = loanInfo.getLoanAmount();

        // lpr信息，即利率变更
        Map<LocalDate, Double> lprMap = convertLprMap(loanInfo);

        // 提前还款信息
        Map<LocalDate, Double> prepaymentMap = convertPrepaymentMap(loanInfo);

        // 还款金额变更信息
        Map<LocalDate, Double> repayAmountChangeMap = convertRepayAmountChangeMap(loanInfo);

        ArrayList<RepayAmountPreMonthInfo> repayAmountPreMonthInfos = new ArrayList<>();

        LocalDate loanStartDate = loanInfo.getLoanStartDate();
        Integer loanRepayDay = loanInfo.getLoanRepayDay();

        // 遍历还款期数
        Double payAmount = null;
        double currentRate = 5.1;
        double remainsPrincipal = loanAmount;

        int repaidPeriod;
        Integer loanPeriod = loanInfo.getLoanPeriod();
        for (repaidPeriod = 1; repaidPeriod <= loanPeriod; repaidPeriod++) {
            // 计算当前还款日
            LocalDate currentRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay)
                    .plusMonths(repaidPeriod);

            // 查询利率
            Double currentLpr = lprMap.get(currentRepayDate);

            // 第一次需要计算 每月还款金额
            if (Objects.isNull(payAmount)) {
                currentRate = Optional.ofNullable(currentLpr).orElse(currentRate);
                payAmount = calculatePrincipalPreMonth(currentRate, loanAmount, loanPeriod, repaidPeriod);
            }

            Double prepayAmount = prepaymentMap.get(currentRepayDate);

            Double repayAmountChange = repayAmountChangeMap.get(currentRepayDate);

            // 利率变更、提前还款 三种场景需要重新计算还款金额
            if (Objects.equals(currentLpr,currentRate) || Objects.nonNull(prepayAmount)){

            }

            // 还款金额变更，需要重新计算缩短的年限
            if (Objects.nonNull(repayAmountChange)){
                payAmount = repayAmountChange;

                // todo


            }


            RepayAmountPreMonthInfo repayAmountPreMonthInfo = new RepayAmountPreMonthInfo();
            repayAmountPreMonthInfo.setRepayDate(currentRepayDate);
            repayAmountPreMonthInfo.setRepayTimes(repaidPeriod);
            repayAmountPreMonthInfo.setRepayPrincipal(payAmount);

            remainsPrincipal =
                    convert2Double(BigDecimal.valueOf(remainsPrincipal).add(BigDecimal.valueOf(payAmount).negate()));
            repayAmountPreMonthInfo.setRemainsPrincipal(remainsPrincipal);

            repayAmountPreMonthInfos.add(repayAmountPreMonthInfo);

            // 第一期利息需要特殊计算
            if (repaidPeriod == 1) {
                final double firstPeriodInterest = firstPeriodInterest(currentRate, loanAmount, loanStartDate,
                        loanRepayDay);
                repayAmountPreMonthInfo.setRepayInterest(firstPeriodInterest);
                repayAmountPreMonthInfo.setRepayAmount(payAmount + firstPeriodInterest);
                continue;
            }

            final double amountPreMonth = calculateRepayAmountPreMonth(currentRate, loanAmount, loanPeriod);
            repayAmountPreMonthInfo.setRepayAmount(amountPreMonth);

            // 每月还款利息 = 每月还款金额 - 每月还款本金
            repayAmountPreMonthInfo.setRepayInterest(convert2Double(BigDecimal.valueOf(amountPreMonth)
                    .add(BigDecimal.valueOf(payAmount).negate())));

        }
    }

    private Map<LocalDate, Double> convertLprMap(LoanInfo loanInfo) {
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos();
        if (CollectionUtils.isEmpty(loanLprInfos)) {
            return Collections.emptyMap();
        }

        return loanLprInfos.stream()
                .collect(Collectors.toMap(LoanLprInfo::getLprDate, LoanLprInfo::getLpr));
    }

    private Map<LocalDate, Double> convertPrepaymentMap(LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return Collections.emptyMap();
        }

        return prepaymentInfos.stream()
                .collect(Collectors.toMap(PrepaymentInfo::getPrepaymentDate, PrepaymentInfo::getPrepaymentAmount));
    }


    private Map<LocalDate, Double> convertRepayAmountChangeMap(LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return Collections.emptyMap();
        }

        return repayAmountChangeInfos.stream()
                .collect(Collectors.toMap(RepayAmountChangeInfo::getChangeDate, RepayAmountChangeInfo::getRepayAmount));
    }


    public static void main(String[] args) {
        LoanInfo loanInfo = new LoanInfo();
        Map<LocalDate, Double> lprMap = loanInfo.getLoanLprInfos().stream()
                .collect(Collectors.toMap(LoanLprInfo::getLprDate, LoanLprInfo::getLpr));

        System.out.println(lprMap);
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

    private static double convert2Double(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static BigDecimal monthRate(double currentRate) {
        final BigDecimal lprDecimal = BigDecimal.valueOf(currentRate);
        return lprDecimal.divide(BigDecimal.valueOf(12), 5, BigDecimal.ROUND_HALF_UP);
    }


}
