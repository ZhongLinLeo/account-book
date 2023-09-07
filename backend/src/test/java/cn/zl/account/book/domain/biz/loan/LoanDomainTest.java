package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.LoanLprInfo;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.*;

public class LoanDomainTest {

    @Test
    public void calculatePrepayment() {
        LoanDomain loanDomain = new LoanDomain();


        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setLoanStartDate(LocalDate.parse("2020-09-27"));
        loanInfo.setLoanAmount(1988000d);
        loanInfo.setLoanPeriod(360);
        loanInfo.setLoanRepayDay(16);
        LoanLprInfo loanLprInfo = new LoanLprInfo();
        loanLprInfo.setLpr(0.051);
        loanLprInfo.setLprDate(LocalDate.parse("2020-10-16"));

        loanInfo.setLoanLprInfos(Collections.singletonList(loanLprInfo));


        loanDomain.calculatePrepayment(loanInfo);

    }
}