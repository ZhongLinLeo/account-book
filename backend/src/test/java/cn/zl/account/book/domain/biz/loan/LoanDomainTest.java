package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.LoanInfo;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class LoanDomainTest {

    @Test
    public void calculatePrepayment() throws Exception {
        LoanDomain loanDomain = new LoanDomain();

        LoanInfo loanInfo = JSON.parseObject(readFile(), LoanInfo.class);

        loanDomain.calculatePrepayment(loanInfo);
    }

    public String readFile() throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "loan/LoanInfo.json");
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
}