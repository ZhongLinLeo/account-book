package cn.zl.account.book.application.enums;

import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.LoanLprInfo;
import cn.zl.account.book.application.info.PrepaymentInfo;
import cn.zl.account.book.application.info.RepayAmountChangeInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
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

    public static CalculateEnum findCalType(LocalDate currentRepayDate, LoanInfo loanInfo, int repaidPeriod) {
        if (isLprChange(currentRepayDate, loanInfo)) {
            return LPR_CHANGE;
        } else if (isPrepay(currentRepayDate, loanInfo)) {
            return PREPAY;
        } else if (isRepayChange(currentRepayDate, loanInfo)) {
            return PAY_AMOUNT_CHANGE;
        } else if (repaidPeriod == 1) {
            return FIRST_INSTALLMENT;
        } else if (Objects.equals(repaidPeriod, loanInfo.getLoanPeriod())) {
            return LAST_INSTALLMENT;
        }

        return NORMAL;
    }

    private static boolean isRepayChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<RepayAmountChangeInfo> repayAmountChangeInfos = loanInfo.getRepayAmountChangeInfos();
        if (CollectionUtils.isEmpty(repayAmountChangeInfos)) {
            return false;
        }

        return repayAmountChangeInfos.stream().anyMatch(e -> {
            LocalDate changeDate = e.getChangeDate();
            return Objects.equals(changeDate, currentRepayDate);
        });
    }

    private static boolean isPrepay(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<PrepaymentInfo> prepaymentInfos = loanInfo.getPrepaymentInfos();
        if (CollectionUtils.isEmpty(prepaymentInfos)) {
            return false;
        }

        return prepaymentInfos.stream().anyMatch(e -> {
            LocalDate prepaymentDate = e.getPrepaymentDate();
            LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
            return prepaymentDate.isBefore(currentRepayDate) && prepaymentDate.isAfter(lastRepayDate);
        });
    }


    private static boolean isLprChange(LocalDate currentRepayDate, LoanInfo loanInfo) {
        List<LoanLprInfo> loanLprInfos = loanInfo.getLoanLprInfos();
        if (CollectionUtils.isEmpty(loanLprInfos)) {
            return false;
        }

        return loanLprInfos.stream().anyMatch(e -> {
            LocalDate lprDate = e.getLprDate();
            LocalDate lastRepayDate = currentRepayDate.plusMonths(-1);
            return lprDate.isBefore(currentRepayDate) && lprDate.isAfter(lastRepayDate);
        });
    }

}
