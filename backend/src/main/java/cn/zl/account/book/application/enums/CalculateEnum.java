package cn.zl.account.book.application.enums;

import cn.zl.account.book.application.info.LoanInfo;

import java.util.Objects;

/**
 * @author lin.zl
 */
public enum CalculateEnum {
    /**
     * LPR change
     */
    LPR_CHANGE,

    /**
     * prepay
     */
    PREPAY,

    /**
     * pay amount
     */
    PAY_AMOUNT_CHANGE,

    /**
     * normal
     */
    NORMAL,

    /**
     * first
     */
    FIRST_INSTALLMENT,

    /**
     * last
     */
    LAST_INSTALLMENT,
    ;

    public static CalculateEnum findCalType( LoanInfo loanInfo, int repaidPeriod) {
         if (repaidPeriod == 1) {
            return FIRST_INSTALLMENT;
        } else if (Objects.equals(repaidPeriod, loanInfo.getLoanPeriod())) {
            return LAST_INSTALLMENT;
        }

        return NORMAL;
    }
}
