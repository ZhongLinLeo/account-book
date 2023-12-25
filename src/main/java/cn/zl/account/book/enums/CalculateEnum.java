package cn.zl.account.book.enums;

import cn.zl.account.book.info.LoanInfo;

import java.util.Objects;

/**
 * @author lin.zl
 */
public enum CalculateEnum {
    /**
     * repay change
     */
    REPAY_CHANGE,

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

    public static CalculateEnum findCalType(LoanInfo loanInfo, int repaidPeriod) {
         if (repaidPeriod == 1) {
            return FIRST_INSTALLMENT;
        } else if (Objects.equals(repaidPeriod, loanInfo.getLoanPeriod())) {
            return LAST_INSTALLMENT;
        }

        return NORMAL;
    }
}
