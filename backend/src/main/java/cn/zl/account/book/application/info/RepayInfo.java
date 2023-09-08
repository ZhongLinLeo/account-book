package cn.zl.account.book.application.info;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class RepayInfo {

    private Double loanAmount;

    private Integer installmentsNumber;

    private Integer repaidNumber;

    private Double repaidInterest;

    private Double repaidPrincipal;

    private Double remainsPrincipal;

    private Double totalInterest;

}
