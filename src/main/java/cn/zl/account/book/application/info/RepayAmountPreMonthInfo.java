package cn.zl.account.book.application.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author lin.zl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepayAmountPreMonthInfo {

    private Integer repayTimes;

    private LocalDate repayDate;

    private Double repayAmount;

    private Double repayInterest;

    private Double repayPrincipal;

    private Double remainsPrincipal;

    private Double currentRate;

    private Integer reduceMonths;

    private PrepaymentInfo prepaymentInfo;

    @Override
    public String toString() {
        return "{" +
                "\"repayTimes\":" + repayTimes +
                ", \"repayDate\":\"" + repayDate + "\"" +
                ", \"repayAmount\":" + repayAmount +
                ", \"repayInterest\":" + repayInterest +
                ", \"repayPrincipal\":" + repayPrincipal +
                ", \"remainsPrincipal\":" + remainsPrincipal +
                '}';
    }
}
