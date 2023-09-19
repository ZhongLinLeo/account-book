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

        final BigDecimal lprDecimal = BigDecimal.valueOf(5.1);
        BigDecimal  monthRate = lprDecimal.divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 5, BigDecimal.ROUND_HALF_UP);

        final BigDecimal tmpPow = BigDecimal.ONE.add(monthRate).pow(360);

        final BigDecimal molecular = BigDecimal.valueOf(1988000).multiply(monthRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 4, BigDecimal.ROUND_HALF_UP);

    }

    @Test
    public void testDay(){

        final BigDecimal lprDecimal = BigDecimal.valueOf(5.1);
        BigDecimal  dayRate = lprDecimal.divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(360), 5, BigDecimal.ROUND_HALF_UP);

        final BigDecimal tmpPow = BigDecimal.ONE.add(dayRate).pow(360);

        final BigDecimal molecular = BigDecimal.valueOf(1988000).multiply(dayRate).multiply(tmpPow);

        final BigDecimal denominator = tmpPow.add(BigDecimal.ONE.negate());

        final BigDecimal repayAmount = molecular.divide(denominator, 4, BigDecimal.ROUND_HALF_UP);

    }



    public String readFile() throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "loan/LoanInfo.json");
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
}