package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.util.concurrent.ConcurrentHashMap;

import static cn.zl.account.book.application.enums.CalculateEnum.*;

/**
 * @author lin.zl
 */
public class LoanCalculateFactory {

    static final ConcurrentHashMap<CalculateEnum, BaseLoanCalculate> CAL_MAP = new ConcurrentHashMap<>();

    static {
        CAL_MAP.put(FIRST_INSTALLMENT, new FirstInstallmentCalculate());
        CAL_MAP.put(NORMAL, new NormalInstallmentCalculate());
        CAL_MAP.put(LAST_INSTALLMENT, new LastInstallmentCalculate());
        CAL_MAP.put(REPAY_CHANGE, new RepayChangeCalculate());
    }

    public static RepayAmountPreMonthInfo calculatePayInfo(LoanInfo loanInfo, LoanCalculateInfo calculateInfo, CalculateEnum calculateEnum) {
        return CAL_MAP.get(calculateEnum).repayCalculate(loanInfo,calculateInfo);
    }

    private LoanCalculateFactory() {
    }
}
