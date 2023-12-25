package cn.zl.account.book.info;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class RepayInfo {

    private Double loanAmount;


    private Integer installmentsNumber;
    private Integer remainsInstallmentsNumber;



    private Integer repaidNumber;

    private Double repaidInterest;

    private Double repaidPrincipal;

    private Double remainsPrincipal;
    private Double remainsInterest;


    private Double totalInterest;

}
