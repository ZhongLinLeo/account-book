package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        Double repayAmount = preMonthInfo.getRepayAmount();
        double currentRate = getCurrentRate(calculateInfo, loanInfo);
        double totalAmount = Optional.ofNullable(preMonthInfo.getRemainsPrincipal())
                .orElse(calculateInfo.getRemainsPrincipal());

        int loanPeriod = calculateInfo.getLoanPeriod() - Optional.ofNullable(preMonthInfo.getReduceMonths()).orElse(0);
        if (Objects.isNull(repayAmount)) {
            repayAmount = repayAmountPreMonth(currentRate, totalAmount, loanPeriod);

        }

        double principalPreMonth = calculatePrincipalPreMonth(currentRate, totalAmount, repayAmount, calculateInfo.getRepaidPeriod());

        // 每月还款利息 = 每月还款金额 - 每月还款本金
        double interest = convert2Double(BigDecimal.valueOf(repayAmount)
                .add(BigDecimal.valueOf(principalPreMonth).negate()));

        preMonthInfo.setRepayAmount(repayAmount);
        preMonthInfo.setRepayInterest(interest);
        preMonthInfo.setRepayPrincipal(principalPreMonth);
        preMonthInfo.setRemainsPrincipal(totalAmount - principalPreMonth);
        preMonthInfo.setCurrentRate(currentRate);

        return preMonthInfo;
    }

    private void payAmountChange(LoanInfo loanInfo, LoanCalculateInfo calculateInfo, RepayAmountPreMonthInfo preMonthInfo) {
        LocalDate currentRepayDate = calculateInfo.getCurrentRepayDate();
        if (!isRepayAmountChange(currentRepayDate, loanInfo)) {
            return;
        }

        double repayAmount = getRepayAmount(calculateInfo, loanInfo);
        double currentRate = getCurrentRate(calculateInfo, loanInfo);
        int loanPeriod = calculateInfo.getLoanPeriod();

        double loanAmount = Optional.ofNullable(preMonthInfo.getRemainsPrincipal()).orElse(calculateInfo.getRemainsPrincipal());

        int reduceMonths = 0;
        for (int period = loanPeriod; period > 0; period--) {
            double amountPreMonth = repayAmountPreMonth(currentRate, loanAmount, period);
            if (BigDecimal.valueOf(amountPreMonth).equals(BigDecimal.valueOf(repayAmount))) {
                reduceMonths = loanPeriod - period + 1;
                break;
            }
        }

        // 杭州银行只能缩短整年的
        reduceMonths = reduceMonths / 12 * 12;

        Integer currentReduce = Optional.ofNullable(preMonthInfo.getReduceMonths()).orElse(0);
        preMonthInfo.setReduceMonths(currentReduce + reduceMonths);
        preMonthInfo.setRepayAmount(repayAmount);
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
        double repayAmount = getRepayAmount(calculateInfo, loanInfo);

        int reduceMonths = reduceMonths(currentRate, remainsPrincipal, calculateInfo.getLoanPeriod(), repayAmount);

        preMonthInfo.setReduceMonths(reduceMonths);
        preMonthInfo.setRemainsPrincipal(remainsPrincipal);
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

        // lpr 改变的时候，不太清楚怎么计算，可以测试一些，变更前一半，变更后一半
        double currentRate = getCurrentRate(calculateInfo, loanInfo);
        double totalAmount = calculateInfo.getRemainsPrincipal();
        int loanPeriod = calculateInfo.getLoanPeriod();

        // 还款金额
        double repayAmount = calculateInfo.getRepayAmount();
        double afterChangePayAmount = repayAmountPreMonth(currentRate, totalAmount, loanPeriod);
        preMonthInfo.setRepayAmount(repayAmount / 2 + afterChangePayAmount / 2);

        // 本金
        double originPrincipal = calculatePrincipalPreMonth(calculateInfo.getCurrentRate(), totalAmount, repayAmount, 1);
        double currentPrincipal = calculatePrincipalPreMonth(currentRate, totalAmount, repayAmount, 1);
        preMonthInfo.setRepayPrincipal((originPrincipal + currentPrincipal) / 2);

        // 利息
        double originInterest = convert2Double(BigDecimal.valueOf(repayAmount)
                .add(BigDecimal.valueOf(originPrincipal).negate()));

        double currentInterest = convert2Double(BigDecimal.valueOf(afterChangePayAmount)
                .add(BigDecimal.valueOf(currentPrincipal).negate()));
        preMonthInfo.setRepayInterest((originInterest + currentInterest) / 2);

        // 利率变更会导致每月还款金额变少
        calculateInfo.setRepayAmount(afterChangePayAmount);
    }

    private double getRepayAmount(LoanCalculateInfo calculateInfo, LoanInfo loanInfo) {
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
            }
        }
        return repayAmount;
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
                reduceMonths = loanPeriod - period + 1;
                break;
            }
        }

        // 杭州银行只能缩短整年的
        return reduceMonths / 12 * 12;
    }

    @Override
    public CalculateEnum calculateType() {
        return CalculateEnum.REPAY_CHANGE;
    }
}
