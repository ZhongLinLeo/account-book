package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 提前还款、利率变更、还款金额变更
 * <p>
 * 首先计算提前还款的，提前还款仅影响本金。将还款计算信息的里面的本金减少即可。同时提前还款部分也要考虑到利率变更的影响，提前还款时的利息与利率有关
 * <p>
 * 其次计算还款金额变更的，还款金额变更，意味着此后每期还款金额变更，会影响到还款期数
 * <p>
 * 最后计算利率变更的。按照当前贷款金额，计算变更前金额与变更后金额
 *
 * @author lin.zl
 */
public class RepayChangeCalculate extends BaseLoanCalculate {

    @Override
    public RepayAmountPreMonthInfo repayCalculate(LoanInfo loanInfo, LoanCalculateInfo calculateInfo) {

        RepayAmountPreMonthInfo preMonthInfo = new RepayAmountPreMonthInfo();

        // 首先计算提前还款的
        prepay(loanInfo, calculateInfo, preMonthInfo);

        // 其次计算还款金额变更的
        payAmountChange(loanInfo, calculateInfo, preMonthInfo);

        // 最后计算利率变更的
        lprChange(loanInfo, calculateInfo, preMonthInfo);

        double currentRate = getCurrentRate(calculateInfo, loanInfo);
        double totalAmount = Optional.ofNullable(preMonthInfo.getRemainsPrincipal())
                .orElse(calculateInfo.getRemainsPrincipal());

        int loanPeriod = calculateInfo.getRemainsPeriod() - Optional.ofNullable(preMonthInfo.getReduceMonths())
                .orElse(0);

        double repayAmount = Optional.ofNullable(calculateInfo.getRepayAmount())
                .orElse(repayAmountPreMonth(currentRate, totalAmount, loanPeriod));
        calculateInfo.setRepayAmount(repayAmount);

        // 利息
        double interest = Optional.ofNullable(preMonthInfo.getRepayInterest())
                .orElse(calculateInterest(totalAmount, currentRate));

        // 本金
        double principalPreMonth = Optional.ofNullable(preMonthInfo.getRepayPrincipal())
                .orElse(calculatePrincipal(repayAmount, interest));

        preMonthInfo.setRepayAmount(convert2Accuracy(interest + principalPreMonth));
        preMonthInfo.setRepayInterest(interest);
        preMonthInfo.setRepayPrincipal(principalPreMonth);
        preMonthInfo.setRemainsPrincipal(convert2Accuracy(totalAmount - principalPreMonth));
        preMonthInfo.setCurrentRate(currentRate);

        return preMonthInfo;
    }

    private void payAmountChange(LoanInfo loanInfo, LoanCalculateInfo calculateInfo, RepayAmountPreMonthInfo preMonthInfo) {
        LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        if (!isRepayAmountChange(currentRepayDate, loanInfo)) {
            return;
        }

        RepayAmountChangeInfo repayAmountChangeInfo = getRepayAmountChangeInfo(calculateInfo, loanInfo);

        Integer currentReduce = Optional.ofNullable(preMonthInfo.getReduceMonths()).orElse(0);
        preMonthInfo.setReduceMonths(repayAmountChangeInfo.getReduceMonths() + currentReduce);
        calculateInfo.setRepayAmount(repayAmountChangeInfo.getRepayAmount());
    }

    private void prepay(LoanInfo loanInfo, LoanCalculateInfo calculateInfo, RepayAmountPreMonthInfo preMonthInfo) {
        LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        if (!isPrepay(currentRepayDate, loanInfo)) {
            return;
        }

        // 提前还款信息
        PrepaymentInfo prepayInfo = calculatePrepayInfo(loanInfo, calculateInfo, currentRepayDate);
        preMonthInfo.setPrepaymentInfo(prepayInfo);

        // 提前还款后，贷款期限缩减，还款金额也会变化
        double remainsPrincipal = calculateInfo.getRemainsPrincipal() - prepayInfo.getPrepaymentAmount();

        double currentRate = getCurrentRate(calculateInfo, loanInfo);
        double repayAmount = getRepayAmount(calculateInfo);

        int loanPeriod = calculateInfo.getRemainsPeriod();
        int reduceMonths = reduceMonths(currentRate, remainsPrincipal, loanPeriod, repayAmount);

        preMonthInfo.setReduceMonths(reduceMonths);
        preMonthInfo.setRemainsPrincipal(remainsPrincipal);

        // 重新计算月还款金额
        repayAmount = repayAmountPreMonth(currentRate, remainsPrincipal, loanPeriod - reduceMonths);
        calculateInfo.setRepayAmount(repayAmount);
    }

    private PrepaymentInfo calculatePrepayInfo(LoanInfo loanInfo, LoanCalculateInfo calculateInfo, LocalDate currentRepayDate) {
        PrepaymentInfo prepayInfo = getPrepayInfo(calculateInfo, loanInfo);

        double currentRate = calculateInfo.getCurrentRate();

        LocalDate prepaymentDate = prepayInfo.getPrepaymentDate();
        if (isLprChange(currentRepayDate, loanInfo)) {
            Optional<LoanLprInfo> lprInfoOpt = loanInfo.getLoanLprInfos().stream()
                    .filter(e -> {
                        LocalDate lprDate = e.getLprDate();
                        return isCurrentPeriod(lprDate, currentRepayDate);
                    })
                    .filter(e -> {
                        LocalDate lprDate = e.getLprDate();

                        return lprDate.isBefore(prepaymentDate);
                    })
                    .findFirst();

            if (lprInfoOpt.isPresent()) {
                LoanLprInfo loanLprInfo = lprInfoOpt.get();

                // 此日期之前按照旧利率计算利息，之后按照新利率计算利息
                LocalDate lprDate = loanLprInfo.getLprDate();

                Period before = Period.between(currentRepayDate.plusMonths(-1), lprDate);
                double interestBefore = daysInterest(currentRate, prepayInfo.getPrepaymentAmount(), before.getDays());

                Period after = Period.between(lprDate, prepaymentDate);
                double interestAfter = daysInterest(currentRate, prepayInfo.getPrepaymentAmount(), after.getDays());
                prepayInfo.setPrepayInterest(interestBefore + interestAfter);
            }
        } else {
            Period interestPeriod = Period.between(currentRepayDate.plusMonths(-1), prepaymentDate);
            double interestAfter = daysInterest(currentRate, prepayInfo.getPrepaymentAmount(), interestPeriod.getDays());
            prepayInfo.setPrepayInterest(interestAfter);
        }

        return prepayInfo;
    }

    private double daysInterest(double currentRate, double amount, int days) {
        BigDecimal daysRate = BigDecimal.valueOf(currentRate / 100 / 12 / 30);
        final BigDecimal lastPeriodInterest = BigDecimal.valueOf(amount)
                .multiply(daysRate).multiply(BigDecimal.valueOf(days));
        return convert2Double(lastPeriodInterest);
    }

    private void lprChange(LoanInfo loanInfo, LoanCalculateInfo calculateInfo, RepayAmountPreMonthInfo preMonthInfo) {
        LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        if (!isLprChange(currentRepayDate, loanInfo)) {
            return;
        }

        double loanAmount = calculateInfo.getRemainsPrincipal();
        int loanPeriod = calculateInfo.getRemainsPeriod();
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos()
                .stream()
                .filter(e -> isCurrentPeriod(e.getLprDate(), currentRepayDate))
                .collect(Collectors.toList());

        double lastRate = calculateInfo.getCurrentRate();

        Map<LocalDate, LoanLprInfo> changeMap = loanLprInfos.stream().collect(Collectors
                .toMap(LoanLprInfo::getLprDate, e -> e, (o, n) -> n));

        List<LocalDate> changeDate = loanLprInfos.stream().map(LoanLprInfo::getLprDate).sorted()
                .collect(Collectors.toList());

        BigDecimal repayAmount = BigDecimal.ZERO;
        BigDecimal repayPrincipal = BigDecimal.ZERO;
        BigDecimal repayInterest = BigDecimal.ZERO;

        BigDecimal lastRepayAmount = BigDecimal.valueOf(repayAmountPreMonth(lastRate, loanAmount, loanPeriod));
        BigDecimal lastRepayInterest = BigDecimal.valueOf(loanAmount).multiply(BigDecimal.valueOf(lastRate / 100 / 12));
        BigDecimal lastRepayPrincipal = lastRepayAmount.add(lastRepayInterest.negate());

        LocalDate lastDate = currentRepayDate.plusMonths(-1);

        int remainsDay = 30;
        double currentRate = lastRate;
        for (LocalDate date : changeDate) {
            LoanLprInfo loanLprInfo = changeMap.get(date);

            int days = lastRateDays(remainsDay,lastDate, date, currentRepayDate);
            repayInterest = repayInterest
                    .add(lastRepayInterest.multiply(BigDecimal.valueOf(days / 30.0)));
            repayPrincipal = repayPrincipal
                    .add(lastRepayPrincipal.multiply(BigDecimal.valueOf(days / 30.0)));
            repayAmount = repayAmount
                    .add(lastRepayAmount.multiply(BigDecimal.valueOf(days / 30.0)));

            Double lpr = loanLprInfo.getLpr();
            lastRepayAmount = BigDecimal.valueOf(repayAmountPreMonth(lpr, loanAmount, loanPeriod));
            lastRepayInterest = BigDecimal.valueOf(loanAmount).multiply(BigDecimal.valueOf(lpr / 100 / 12));
            lastRepayPrincipal = lastRepayAmount.add(lastRepayInterest.negate());
            lastDate = date;
            remainsDay -= days;
            currentRate = loanLprInfo.getLpr();
        }

        if (remainsDay >= 0) {
            repayInterest = repayInterest
                    .add(lastRepayInterest.multiply(BigDecimal.valueOf(remainsDay / 30.0)));
            repayPrincipal = repayPrincipal
                    .add(lastRepayPrincipal.multiply(BigDecimal.valueOf(remainsDay / 30.0)));
            repayAmount = repayAmount
                    .add(lastRepayAmount.multiply(BigDecimal.valueOf(remainsDay / 30.0)));
        }


        preMonthInfo.setRepayAmount(convert2Double(repayAmount));
        preMonthInfo.setRepayPrincipal(convert2Double(repayPrincipal));
        preMonthInfo.setRepayInterest(convert2Double(repayInterest));

        // 利率变更会导致每月还款金额变少
        double nextRepayAmount = repayAmountPreMonth(currentRate, loanAmount - preMonthInfo.getRepayPrincipal(),
                loanPeriod - 1);
        calculateInfo.setRepayAmount(nextRepayAmount);
    }

    private int lastRateDays(int remainsDay,LocalDate lastDate, LocalDate date, LocalDate currentRepayDate) {
        if (lastDate.getMonth().equals(date.getMonth())) {
            return Period.between(lastDate, date).getDays();
        }

        return remainsDay - Period.between(date, currentRepayDate).getDays();
    }

    private RepayAmountChangeInfo getRepayAmountChangeInfo(LoanCalculateInfo calculateInfo, LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();

        final LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        for (RepayAmountChangeInfo repayAmountChangeInfo : repayAmountChangeInfos) {
            LocalDate changeDate = repayAmountChangeInfo.getChangeDate();
            if (isCurrentPeriod(changeDate, currentRepayDate)) {
                return repayAmountChangeInfo;
            }
        }
        throw new RuntimeException();
    }

    private PrepaymentInfo getPrepayInfo(LoanCalculateInfo calculateInfo, LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        final LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        for (PrepaymentInfo prepaymentInfo : prepaymentInfos) {
            LocalDate prepaymentDate = prepaymentInfo.getPrepaymentDate();
            if (isCurrentPeriod(prepaymentDate, currentRepayDate)) {
                return prepaymentInfo;
            }
        }
        throw new RuntimeException();
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

    private int reduceMonths(double rate, double loanAmount, int loanPeriod, double originPayAmount) {
        // 暴力破解
        int reduceMonths = 0;
        for (int period = loanPeriod; period > 0; period--) {
            double amountPreMonth = repayAmountPreMonth(rate, loanAmount, period);
            if (amountPreMonth > originPayAmount) {
                break;
            }
            reduceMonths = loanPeriod - period;
        }

        // 杭州银行只能缩短整年的
        return reduceMonths / 12 * 12;
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.REPAY_CHANGE;
    }
}
