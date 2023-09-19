package cn.zl.account.book.domain.biz.loan;

import cn.zl.account.book.application.enums.CalculateEnum;
import cn.zl.account.book.application.info.LoanCalculateInfo;
import cn.zl.account.book.application.info.LoanInfo;
import cn.zl.account.book.application.info.RepayAmountPreMonthInfo;
import cn.zl.account.book.application.info.RepayInfo;
import cn.zl.account.book.domain.biz.loan.factory.LoanCalculateFactory;
import com.alibaba.fastjson2.JSON;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * loan calculate
 * <p>
 * 1. 只有利率、金额有变更的时候，才会重新计算还款金额
 * 2. 杭州银行只能减少整年的
 * <p>
 * <p>
 * 想了解的信息
 * 1、每月还款金额 -- done
 * 2、已还金额、已还本金、已还利息、已还期数、剩余期数，这个可以通过计算已还信息得来
 * 3、利率、本金、还款金额变更后 每月还款信息 -- 变更后重新计算还款金额
 *
 * @author lin.zl
 */
public class LoanDomain {


    public void calculatePrepayment(LoanInfo loanInfo) {
        // calculate Interest
        final List<RepayAmountPreMonthInfo> originRepayInfos = calRepayInfo(loanInfo);

        System.out.println(JSON.toJSONString(originRepayInfos));

        RepayInfo repayInfo = new RepayInfo();

        repayInfo.setLoanAmount(loanInfo.getLoanAmount());
        repayInfo.setInstallmentsNumber(loanInfo.getLoanPeriod());

        LocalDate now = LocalDate.now();
        List<RepayAmountPreMonthInfo> repaidList = originRepayInfos.stream()
                .filter(repayAmount -> repayAmount.getRepayDate().isBefore(now)).collect(Collectors.toList());

        repayInfo.setRepaidNumber(repaidList.size());
        repayInfo.setRepaidInterest(repaidList.stream().mapToDouble(RepayAmountPreMonthInfo::getRepayInterest).sum());
        double repaidPrincipal = repaidList.stream().mapToDouble(RepayAmountPreMonthInfo::getRepayPrincipal).sum();
        repayInfo.setRepaidPrincipal(repaidPrincipal);
        repayInfo.setRemainsPrincipal(BigDecimal.valueOf(loanInfo.getLoanAmount())
                .add(BigDecimal.valueOf(repaidPrincipal).negate()).doubleValue());

        repayInfo.setTotalInterest(originRepayInfos.stream()
                .mapToDouble(RepayAmountPreMonthInfo::getRepayInterest).sum());

        System.out.println(repayInfo);
    }

    public List<RepayAmountPreMonthInfo> calRepayInfo(LoanInfo loanInfo) {

        List<RepayAmountPreMonthInfo> repayAmountPreMonthInfos = new ArrayList<>();

        LocalDate loanStartDate = loanInfo.getLoanStartDate();
        Integer loanRepayDay = loanInfo.getLoanRepayDay();

        int repaidPeriod;
        Integer loanPeriod = loanInfo.getLoanPeriod();

        LoanCalculateInfo calInfo = new LoanCalculateInfo();
        calInfo.setLoanStartDate(loanInfo.getLoanStartDate());
        calInfo.setCurrentRate(loanInfo.getLoanRate());
        calInfo.setLoanRepayDay(loanInfo.getLoanRepayDay());

        double remainsPrincipal = loanInfo.getLoanAmount();

        // 遍历还款期数
        for (repaidPeriod = 1; repaidPeriod <= loanPeriod; repaidPeriod++) {
            // 计算当前还款日
            LocalDate currentRepayDate = LocalDate.of(loanStartDate.getYear(), loanStartDate.getMonth(), loanRepayDay)
                    .plusMonths(repaidPeriod);

            CalculateEnum calType = CalculateEnum.findCalType(currentRepayDate, loanInfo, repaidPeriod);

            calInfo.setCurrentRepayDate(currentRepayDate);
            calInfo.setLoanPeriod(loanPeriod - repaidPeriod + 1);
            calInfo.setRemainsPrincipal(remainsPrincipal);

            RepayAmountPreMonthInfo repayAmountPreMonthInfo = LoanCalculateFactory.calculatePayInfo(loanInfo, calInfo, calType);

            repayAmountPreMonthInfo.setRepayTimes(repaidPeriod);
            remainsPrincipal = repayAmountPreMonthInfo.getRemainsPrincipal();

            repayAmountPreMonthInfos.add(repayAmountPreMonthInfo);

            loanPeriod -= Optional.ofNullable(repayAmountPreMonthInfo.getReduceMonths()).orElse(0);
        }
        return repayAmountPreMonthInfos;
    }
}
