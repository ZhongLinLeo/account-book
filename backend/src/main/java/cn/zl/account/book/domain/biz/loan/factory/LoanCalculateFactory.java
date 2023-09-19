package cn.zl.account.book.domain.biz.loan.factory;

import cn.zl.account.book.application.enums.LoanChangeEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;

import java.util.concurrent.ConcurrentHashMap;

import static cn.zl.account.book.application.enums.LoanChangeEnum.*;

/**
 * @author lin.zl
 */
public class LoanCalculateFactory {

    static final ConcurrentHashMap<LoanChangeEnum, BaseLoanCalculate> calMap = new ConcurrentHashMap<>();

    static {
        calMap.put(FIRST_INSTALLMENT, new FirstInstallmentCalculate());
    }

    public static RepayAmountPreMonthInfo calculatePayInfo(LoanCalculateInfo calculateInfo, LoanChangeEnum loanChange) {
        return calMap.get(loanChange).repayCalculate(calculateInfo);
    }

}
