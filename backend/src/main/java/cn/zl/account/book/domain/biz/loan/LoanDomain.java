package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.LoanLprInfo;
import cn.zl.account.book.application.info.RepayLoanInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * loan calculate
 *
 * @author lin.zl
 */
public class LoanDomain {


    public void calculatePrepayment(LoanInfo loanInfo) {
        // calculate Interest
        calculateRepayInfo(loanInfo);


    }

    private List<RepayLoanInfo> calculateRepayInfo(LoanInfo loanInfo) {

        ArrayList<RepayLoanInfo> repayLoanInfos = new ArrayList<>();

        Map<LocalDate, Double> rateMap = loanInfo
                .getLoanLprInfos()
                .stream()
                .collect(Collectors.toMap(LoanLprInfo::getLprDate, LoanLprInfo::getLpr));

        LocalDate loanStartDate = loanInfo.getLoanStartDate();
        Integer loanRepayDay = loanInfo.getLoanRepayDay();
        Double loanAmount = loanInfo.getLoanAmount();

        // 第一个月特殊计算
        LocalDate nexRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay)
                .plusMonths(1);
        Double currentRate = rateMap.get(nexRepayDate);

        RepayLoanInfo firstRepay = new RepayLoanInfo();
        firstRepay.setRepayDate(nexRepayDate);
        firstRepay.setRepayTimes(1);

        Period period = Period.between(loanStartDate, nexRepayDate);

        BigDecimal bigDecimal = repayAmountWithDays(currentRate, BigDecimal.valueOf(loanAmount), period.getDays());
        firstRepay.setRepayInterest(convert2Double(bigDecimal));


        Integer loanPeriod = loanInfo.getLoanPeriod();
        for (int times = 1; times < loanPeriod; times++) {


        }


        return repayLoanInfos;

    }

    private static double convert2Double(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private  BigDecimal convert2Approximate(BigDecimal value) {
        return value.divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
    }
    private BigDecimal repayAmountWithDays(double currentRate, BigDecimal remainingPrincipal, Integer days) {
        final BigDecimal lprDecimal = BigDecimal.valueOf(currentRate);
        BigDecimal dayRate = lprDecimal.divide(BigDecimal.valueOf(360), 5, BigDecimal.ROUND_HALF_UP);
        return remainingPrincipal.multiply(dayRate).multiply(BigDecimal.valueOf(days));
    }

    private  BigDecimal calculateMonthInterest(double currentRate, BigDecimal remainingPrincipal) {
        final BigDecimal monthRate = monthRate(currentRate);
        return remainingPrincipal.multiply(monthRate);
    }

    private static BigDecimal monthRate(double currentRate) {
        final BigDecimal lprDecimal = BigDecimal.valueOf(currentRate);
        return lprDecimal.divide(BigDecimal.valueOf(12), 5, BigDecimal.ROUND_HALF_UP);
    }


}
