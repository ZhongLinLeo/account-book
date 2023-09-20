package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.LoanInfo;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

public class LoanDomainTest {

    @Test
    public void calculatePrepayment() throws Exception {
        LoanDomain loanDomain = new LoanDomain();

        LoanInfo loanInfo = JSON.parseObject(readFile(), LoanInfo.class);

        loanDomain.calculatePrepayment(loanInfo);
    }

    @Test
    public void testMonth(){

        BigDecimal loanRateMonth = BigDecimal.valueOf(4.75 / 100 / 12) ;
        BigDecimal factor = BigDecimal.valueOf(Math.pow(1 + loanRateMonth.doubleValue(), 336));
        BigDecimal avgRepayment = BigDecimal.valueOf(1928885.68).multiply(loanRateMonth)
                .multiply(factor).divide(factor.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);

        System.out.println(avgRepayment);
    }

    @Test
    public void testDay(){

        int loanPeriod = 301;

        double rate = 4.75;
        double loanAmount = 1677112.82;

        double originPayAmount = 10227.85;

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
        int i = reduceMonths / 12 * 12;

        System.out.println(i);
    }

    private  double repayAmountPreMonth(double rate, double loanAmount, int loanPeriod) {

        final BigDecimal monthRate = BigDecimal.valueOf(rate / 100 / 12);

        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(loanPeriod);

        final BigDecimal molecular = BigDecimal.valueOf(loanAmount).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
        return repayAmount.doubleValue();
    }




    public String readFile() throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "loan/LoanInfo.json");
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
}