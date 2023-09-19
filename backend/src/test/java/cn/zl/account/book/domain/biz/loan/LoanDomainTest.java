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

        final BigDecimal lprDecimal = BigDecimal.valueOf(4.75);
//        final BigDecimal monthRate = lprDecimal.divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12));
        final BigDecimal monthRate = BigDecimal.valueOf(4.75/100/12);


        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(336);

        final BigDecimal molecular = BigDecimal.valueOf(1928885.68).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(repayAmount);
    }



    public String readFile() throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "loan/LoanInfo.json");
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
}