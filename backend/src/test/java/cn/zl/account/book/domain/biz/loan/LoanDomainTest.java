package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.LoanLprInfo;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoanDomainTest {

    @Test
    public void calculatePrepayment() throws Exception {
        LoanDomain loanDomain = new LoanDomain();

        LoanInfo loanInfo = JSON.parseObject(readFile(), LoanInfo.class);

        loanDomain.calculatePrepayment(loanInfo);
    }

    @Test
    public void testMonth() {

        BigDecimal loanRateMonth = BigDecimal.valueOf(4.75 / 100 / 12);
        BigDecimal factor = BigDecimal.valueOf(Math.pow(1 + loanRateMonth.doubleValue(), 336));
        BigDecimal avgRepayment = BigDecimal.valueOf(1928885.68).multiply(loanRateMonth)
                .multiply(factor).divide(factor.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);

        System.out.println(avgRepayment);
    }

    @Test
    public void testDay() {

        int loanPeriod = 301;

        double rate = 4.75;
        double loanAmount = 1677112.05;

        double originPayAmount = 10227.85;

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
        int i = reduceMonths / 12 * 12;

        System.out.println(i);
    }

    private double repayAmountPreMonth(double rate, double loanAmount, int loanPeriod) {

//        final BigDecimal monthRate = BigDecimal.valueOf(rate / 100 / 12);
        final BigDecimal monthRate = BigDecimal.valueOf(rate).divide(BigDecimal.valueOf(100 * 12), 7, BigDecimal.ROUND_HALF_UP);


        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(loanPeriod);

        final BigDecimal molecular = BigDecimal.valueOf(loanAmount).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 6, BigDecimal.ROUND_HALF_UP);
        return repayAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Test
    public void lprChange() {

        double loanAmount = 1672939.66;
        int loanPeriod = 240;

        Map<LocalDate, Double> rateMap = new HashMap<>(8);
        rateMap.put(LocalDate.parse("2023-09-25"), 4.3);
//        rateMap.put(LocalDate.parse("2023-10-01"),4.2);

        ArrayList<LoanLprInfo> loanLprInfos = new ArrayList<>();
        loanLprInfos.add(new LoanLprInfo(LocalDate.parse("2023-09-25"), 4.3));
        loanLprInfos.add(new LoanLprInfo(LocalDate.parse("2023-10-01"), 4.2));


        LocalDate currentRepayDate = LocalDate.parse("2023-10-16");

        double originPayAmount = repayAmountPreMonth(4.75, loanAmount, loanPeriod);
        BigDecimal beforeInterest = BigDecimal.valueOf(loanAmount).multiply(BigDecimal.valueOf(4.75 / 100 / 12));
        BigDecimal beforePrincipal = BigDecimal.valueOf(originPayAmount).add(beforeInterest.negate());

        int originRateDays = Period.between(currentRepayDate.plusMonths(-1), LocalDate.parse("2023-09-24")).getDays();

        double afterPayAmount = repayAmountPreMonth(4.3, loanAmount, loanPeriod);
        BigDecimal afterInterest = BigDecimal.valueOf(loanAmount).multiply(BigDecimal.valueOf(4.3 / 100 / 12));
        BigDecimal afterPrincipal = BigDecimal.valueOf(afterPayAmount).add(afterInterest.negate());

        double interest = beforeInterest.multiply(BigDecimal.valueOf(originRateDays / 30.0))
                .add(afterInterest.multiply(BigDecimal.valueOf((30 - originRateDays) / 30.0))).doubleValue();

        double principal = beforePrincipal.multiply(BigDecimal.valueOf(originRateDays / 30.0))
                .add(afterPrincipal.multiply(BigDecimal.valueOf((30 - originRateDays) / 30.0))).doubleValue();


        double amount = BigDecimal.valueOf(originPayAmount).multiply(BigDecimal.valueOf(originRateDays / 30.0))
                .add(BigDecimal.valueOf(afterPayAmount).multiply(BigDecimal.valueOf((30 - originRateDays) / 30.0))).doubleValue();

        System.out.println("interest:" + interest);
        System.out.println("principal:" + principal);
        System.out.println("amount:" + amount);


    }

    @Test
    public void lprChange1() {

        double loanAmount = 1672939.66;
        int loanPeriod = 240;

        Map<LocalDate, Double> rateMap = new HashMap<>(8);
        rateMap.put(LocalDate.parse("2023-09-25"), 4.3);
//        rateMap.put(LocalDate.parse("2023-10-01"),4.2);

        ArrayList<LoanLprInfo> loanLprInfos = new ArrayList<>();
        loanLprInfos.add(new LoanLprInfo(LocalDate.parse("2023-09-25"), 4.3));
//        loanLprInfos.add(new LoanLprInfo(LocalDate.parse("2023-10-01"), 4.2));

        LocalDate currentRepayDate = LocalDate.parse("2023-10-16");

        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal principal = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;


        Map<LocalDate, BigDecimal> interestMap = new HashMap<>();
        Map<LocalDate, BigDecimal> principalMap = new HashMap<>();
        Map<LocalDate, Double> amountMap = new HashMap<>();

        double lastRate = 4.75;

        double originPayAmount = repayAmountPreMonth(lastRate, loanAmount, loanPeriod);
        BigDecimal beforeInterest = BigDecimal.valueOf(loanAmount).multiply(BigDecimal.valueOf(4.75 / 100 / 12));
        BigDecimal beforePrincipal = BigDecimal.valueOf(originPayAmount).add(beforeInterest.negate());

        LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
        interestMap.put(lastRepayDate, beforeInterest);
        principalMap.put(lastRepayDate, beforePrincipal);
        amountMap.put(lastRepayDate, originPayAmount);


        for (LoanLprInfo loanLprInfo : loanLprInfos) {
            double afterPayAmount = repayAmountPreMonth(loanLprInfo.getLpr(), loanAmount, loanPeriod);
            BigDecimal afterInterest = BigDecimal.valueOf(loanAmount).multiply(BigDecimal.valueOf(4.3 / 100 / 12));
            BigDecimal afterPrincipal = BigDecimal.valueOf(afterPayAmount).add(afterInterest.negate());
            interestMap.put(loanLprInfo.getLprDate(), afterInterest);
            principalMap.put(loanLprInfo.getLprDate(), afterPrincipal);
            amountMap.put(loanLprInfo.getLprDate(), afterPayAmount);
        }

        List<LocalDate> dates = amountMap.keySet().stream().sorted().collect(Collectors.toList());

        for (LocalDate date : dates) {

            BigDecimal currentInterest = interestMap.get(date);
            BigDecimal currentPrincipal = principalMap.get(date);
            BigDecimal currentAmount = BigDecimal.valueOf(amountMap.get(date));


            int days = Period.between(lastRepayDate, date).getDays();
            interest = interest
                    .add(currentInterest.multiply(BigDecimal.valueOf(days / 30.0)));
            principal = principal
                    .add(currentPrincipal.multiply(BigDecimal.valueOf(days / 30.0)));
            amount = amount
                    .add(currentAmount.multiply(BigDecimal.valueOf(days / 30.0)));


        }

        System.out.println("interest:" + interest);
        System.out.println("principal:" + principal);
        System.out.println("amount:" + amount);
    }


    public String readFile() throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "loan/LoanInfo.json");
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
}