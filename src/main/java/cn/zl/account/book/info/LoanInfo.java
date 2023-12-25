package cn.zl.account.book.info;

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
     * 贷款初始利率
     */
    private Double loanRate;

    /**
     * 利率
     */
    private List<LoanLprInfo> loanLprInfos;

    /**
     * 提前还款
     */
    private List<PrepaymentInfo> prepaymentInfos;

    /**
     * 还款金额变更
     */
    private List<RepayAmountChangeInfo> repayAmountChangeInfos;

    /**
     * todo
     */
    private String prepaymentType;

}
