package cn.zl.account.book.application.info;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author lin.zl
 */
@Data
public class RepayLoanInfo {

    private Integer repayTimes;

    private LocalDate repayDate;

    private Double repayAmount;

    private Double repayInterest;

    private Double repayPrincipal;

    private Double remainingPrincipal;

    @Override
    public String toString() {
        return "{" +
                "\"repayTimes\":" + repayTimes +
                ", \"repayDate\":\"" + repayDate + "\"" +
                ", \"repayAmount\":" + repayAmount +
                ", \"repayInterest\":" + repayInterest +
                ", \"repayPrincipal\":" + repayPrincipal +
                ", \"remainingPrincipal\":" + remainingPrincipal +
                '}';
    }
}
