package cn.zl.account.book.application.info;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lin.zl
 */
@Data
public class LoanInfo {

    /**
     * 贷款金额
     */
    private Double loanAmount;

    /**
     * 贷款时长
     */
    private Integer loanPeriod;

    /**
     * 贷款发放时间
     */
    private LocalDate loanStartDate;


    /**
     * 还款日
     */
    private Integer loanRepayDay;

    /**
     * 利率
     */
    private List<LoanLprInfo> loanLprInfos;

    /**
     * 提前还款
     */
    private List<PrepaymentInfo> prepaymentInfos;

    /**
     * todo
     */
    private String prepaymentType;

}
