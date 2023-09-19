package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.*;
import cn.zl.account.book.domain.biz.loan.factory.LoanCalculateFactory;
import com.alibaba.fastjson2.JSON;
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
        final List<RepayAmountPreMonthInfo> originRepayInfos = calRepayInfo(loanInfo);

        System.out.println(JSON.toJSONString(originRepayInfos));

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

    public List<RepayAmountPreMonthInfo> calRepayInfo(LoanInfo loanInfo) {

        List<RepayAmountPreMonthInfo> repayAmountPreMonthInfos = new ArrayList<>();

        LocalDate loanStartDate = loanInfo.getLoanStartDate();
        Integer loanRepayDay = loanInfo.getLoanRepayDay();

        int repaidPeriod;
        Integer loanPeriod = loanInfo.getLoanPeriod();

        LoanCalculateInfo calInfo = new LoanCalculateInfo();
        calInfo.setLoanStartDate(loanInfo.getLoanStartDate());
        calInfo.setCurrentRate(loanInfo.getLoanRate());
        calInfo.setLoanRepayDay(loanInfo.getLoanRepayDay());

        double remainsPrincipal = loanInfo.getLoanAmount();

        // 遍历还款期数
        for (repaidPeriod = 1; repaidPeriod <= loanPeriod; repaidPeriod++) {
            // 计算当前还款日
            LocalDate currentRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay)
                    .plusMonths(repaidPeriod);

            CalculateEnum calType = CalculateEnum.findCalType(currentRepayDate, loanInfo, repaidPeriod);

            calInfo.setCurrentRepayDate(currentRepayDate);
            calInfo.setLoanPeriod(loanPeriod - repaidPeriod + 1);
            calInfo.setRemainsPrincipal(remainsPrincipal);

            RepayAmountPreMonthInfo repayAmountPreMonthInfo = LoanCalculateFactory.calculatePayInfo(loanInfo,calInfo, calType);

            repayAmountPreMonthInfo.setRepayTimes(repaidPeriod);
            remainsPrincipal -= repayAmountPreMonthInfo.getRepayPrincipal();
            repayAmountPreMonthInfo.setRemainsPrincipal(remainsPrincipal);

            repayAmountPreMonthInfos.add(repayAmountPreMonthInfo);
        }
        return repayAmountPreMonthInfos;
    }

    public List<RepayAmountPreMonthInfo> calculatePrepaymentInfo(LoanInfo loanInfo) {
        // 贷款金额是不变的
        Double loanAmount = loanInfo.getLoanAmount();

        // lpr信息，即利率变更
        Map<LocalDate, LoanLprInfo> lprMap = convertLprMap(loanInfo);

        // 提前还款信息
        Map<LocalDate, Double> prepaymentMap = convertPrepaymentMap(loanInfo);

        // 还款金额变更信息
        Map<LocalDate, Double> repayAmountChangeMap = convertRepayAmountChangeMap(loanInfo);

        List<RepayAmountPreMonthInfo> repayAmountPreMonthInfos = new ArrayList<>();

        LocalDate loanStartDate = loanInfo.getLoanStartDate();
        Integer loanRepayDay = loanInfo.getLoanRepayDay();

        Double payAmount = null;
        double currentRate = 5.1;
        double remainsPrincipal = loanAmount;

        int repaidPeriod;
        Integer loanPeriod = loanInfo.getLoanPeriod();

        // 遍历还款期数
        for (repaidPeriod = 1; repaidPeriod <= loanPeriod; repaidPeriod++) {
            // 计算当前还款日
            LocalDate currentRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay)
                    .plusMonths(repaidPeriod);

            // 查询利率
            Double currentLpr = getCurrentLpr(lprMap, currentRate, currentRepayDate);

            // 第一次需要计算 每月还款金额
            if (Objects.isNull(payAmount)) {
                currentRate = Optional.ofNullable(currentLpr).orElse(currentRate);
                payAmount = repayAmountPreMonth(currentRate, remainsPrincipal, loanPeriod - repaidPeriod + 1);
            }

            // 利率变更，不会缩短期数，只会缩短每月还款金额
            if (Objects.nonNull(currentLpr) && !Objects.equals(currentLpr, currentRate)) {
                currentRate = currentLpr;
                payAmount = repayAmountPreMonth(currentRate, remainsPrincipal, loanPeriod - repaidPeriod + 1);
            }

            // 提前还款,目前只考虑缩短年限，杭州银行仅支持缩短整年
            Double prepayAmount = prepaymentMap.get(currentRepayDate);
            if (Objects.nonNull(prepayAmount)) {
                int prepayReduceMonths = prepayReduceMonths(prepayAmount, currentRate, loanAmount, loanPeriod, payAmount, repaidPeriod);

                // 提前还款之后，剩余的还款期数
                loanPeriod -= prepayReduceMonths;

                // 提前还款后，当前金额
                payAmount = repayAmountPreMonth(currentRate, loanAmount - prepayAmount, loanPeriod - repaidPeriod);
            }

            // 还款金额变更，会缩短期数
            Double repayAmountChange = repayAmountChangeMap.get(currentRepayDate);
            if (Objects.nonNull(repayAmountChange)) {
                payAmount = repayAmountChange;
                loanPeriod -= repaidPeriod;
                int reduceMonths = reduceMonths(currentRate, loanAmount, payAmount, loanPeriod, payAmount);
                loanPeriod -= reduceMonths;
            }

            RepayAmountPreMonthInfo repayAmountPreMonthInfo = new RepayAmountPreMonthInfo();
            repayAmountPreMonthInfo.setRepayDate(currentRepayDate);
            repayAmountPreMonthInfo.setRepayTimes(repaidPeriod);

            double principalPreMonth = calculatePrincipalPreMonth(currentRate, loanAmount, payAmount, repaidPeriod);
            repayAmountPreMonthInfo.setRepayPrincipal(principalPreMonth);

            remainsPrincipal =
                    convert2Double(BigDecimal.valueOf(remainsPrincipal).add(BigDecimal.valueOf(principalPreMonth).negate()));
            repayAmountPreMonthInfo.setRemainsPrincipal(remainsPrincipal);

            repayAmountPreMonthInfos.add(repayAmountPreMonthInfo);

            // 第一期利息需要特殊计算
            if (repaidPeriod == 1) {
                final double firstPeriodInterest = firstPeriodInterest(currentRate, loanAmount, loanStartDate,
                        loanRepayDay);
                repayAmountPreMonthInfo.setRepayInterest(firstPeriodInterest);
                repayAmountPreMonthInfo.setRepayAmount(principalPreMonth + firstPeriodInterest);
                continue;
            }

            // 最后一次还款也需要特殊计算
            if (repaidPeriod == loanPeriod) {
                double lastPeriodInterest = lastPeriodInterest(currentRate, remainsPrincipal);
                repayAmountPreMonthInfo.setRepayInterest(lastPeriodInterest);
                repayAmountPreMonthInfo.setRepayAmount(remainsPrincipal);
                continue;
            }

            repayAmountPreMonthInfo.setRepayAmount(payAmount);

            // 每月还款利息 = 每月还款金额 - 每月还款本金
            repayAmountPreMonthInfo.setRepayInterest(convert2Double(BigDecimal.valueOf(payAmount)
                    .add(BigDecimal.valueOf(principalPreMonth).negate())));
        }

        return repayAmountPreMonthInfos;
    }

    private static double rateChange(double originRate, double currentRate, double remainsPrincipal, int loanPeriod) {
        // 部分按照之前利率计算，部分按照之后的利率计算, 10592.12,2675.68,7916.44, 2022-10-16

        LocalDate lprChangeDate = LocalDate.parse("2022-09-27");
        final LocalDate repayDate = LocalDate.parse("2022-10-16");
//        Period period = Period.between(repayDate, lprChangeDate);

        int interestDay = 30 - 15;
        final BigDecimal upHalf = BigDecimal.valueOf(remainsPrincipal)
                .multiply(monthRate(originRate))
                .multiply(BigDecimal.valueOf(interestDay))
                .divide(BigDecimal.valueOf(30), 4, BigDecimal.ROUND_HALF_UP);
        double v1 = convert2Double(upHalf);

        final BigDecimal downHalf = BigDecimal.valueOf(remainsPrincipal)
                .multiply(monthRate(currentRate))
                .multiply(BigDecimal.valueOf(30 - interestDay))
                .divide(BigDecimal.valueOf(30), 4, BigDecimal.ROUND_HALF_UP);
        double v2 = convert2Double(downHalf);

        double v = v1 + v2;
        return v;
    }

    private Double getCurrentLpr(Map<LocalDate, LoanLprInfo> lprMap, double currentRate, LocalDate currentRepayDate) {
        Double currentLpr = currentRate;
        LoanLprInfo loanLprInfo = lprMap.get(currentRepayDate);
        if (Objects.nonNull(loanLprInfo)) {
            currentLpr = loanLprInfo.getLpr();
        }
        return currentLpr;
    }

    private int prepayReduceMonths(double prepayAmount, double rate, double loanAmount,
                                   int loanPeriod, double originPayAmount, double repaidPeriod) {
        // 提前还款,目前只考虑缩短年限，杭州银行仅支持缩短整年
        loanPeriod -= repaidPeriod;
        return reduceMonths(rate, loanAmount, prepayAmount, loanPeriod, originPayAmount);
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
    private double calculatePrincipalPreMonth(double rate, double loanAmount, double payAmount, int times) {
        final BigDecimal monthRate = monthRate(rate);

        final BigDecimal tmp = BigDecimal.valueOf(loanAmount).multiply(monthRate);

        final BigDecimal firstPrincipal = BigDecimal.valueOf(payAmount).add(tmp.negate());

        // Pn = P1 * (1 + β) ^ (n - 1)

        final BigDecimal principal = firstPrincipal.multiply(BigDecimal.ONE.add(monthRate).pow(times - 1));
        return convert2Double(principal);
    }


    private double lastPeriodInterest(double currentRate, double amount) {
        final BigDecimal lastPeriodInterest = BigDecimal.valueOf(amount)
                .multiply(monthRate(currentRate));
        return convert2Double(lastPeriodInterest);
    }


    private Map<LocalDate, LoanLprInfo> convertLprMap(LoanInfo loanInfo) {
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos();
        if (CollectionUtils.isEmpty(loanLprInfos)) {
            return Collections.emptyMap();
        }

        return loanLprInfos.stream()
                .collect(Collectors.toMap(e -> {
                    LocalDate lprDate = e.getLprDate();
                    return lprDate.withDayOfMonth(loanInfo.getLoanRepayDay());
                }, e -> e));
    }

    private Map<LocalDate, Double> convertPrepaymentMap(LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return Collections.emptyMap();
        }

        return prepaymentInfos.stream()
                .collect(Collectors.toMap(e -> {
                    LocalDate prepaymentDate = e.getPrepaymentDate();
                    return prepaymentDate.withDayOfMonth(loanInfo.getLoanRepayDay());
                }, PrepaymentInfo::getPrepaymentAmount));
    }


    private Map<LocalDate, Double> convertRepayAmountChangeMap(LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return Collections.emptyMap();
        }

        return repayAmountChangeInfos.stream()
                .collect(Collectors.toMap(RepayAmountChangeInfo::getChangeDate, RepayAmountChangeInfo::getRepayAmount));
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

            final double amountPreMonth = repayAmountPreMonth(currentRate, loanAmount, loanPeriod);
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
    private double repayAmountPreMonth(double rate, double loanAmount, int loanPeriod) {

        final BigDecimal monthRate = BigDecimal.valueOf(rate / 100 / 12);

        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(loanPeriod);

        final BigDecimal molecular = BigDecimal.valueOf(loanAmount).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 4, BigDecimal.ROUND_HALF_UP);
        return convert2Double(repayAmount);
    }

    /**
     * 计算 还款本金
     *
     * @param rate       年利率
     * @param loanAmount 贷款金额
     * @param loanPeriod 贷款期限
     * @param times      还款期数
     * @return 本金
     */
    private double calculatePrincipalPreMonth(double rate, double loanAmount, int loanPeriod, int times) {
        final BigDecimal monthRate = monthRate(rate);

        final double amountPreMonth = repayAmountPreMonth(rate, loanAmount, loanPeriod);

        final BigDecimal tmp = BigDecimal.valueOf(loanAmount).multiply(monthRate);

        final BigDecimal firstPrincipal = BigDecimal.valueOf(amountPreMonth).add(tmp.negate());

        // Pn = P1 * (1 + β) ^ (n - 1)

        final BigDecimal principal = firstPrincipal.multiply(BigDecimal.ONE.add(monthRate).pow(times - 1));
        return convert2Double(principal);
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
        return lprDecimal.divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 5, BigDecimal.ROUND_HALF_UP);
    }


}
