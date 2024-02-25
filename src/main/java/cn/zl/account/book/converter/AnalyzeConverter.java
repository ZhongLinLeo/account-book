package cn.zl.account.book.converter;

import cn.zl.account.book.info.FundsComposeInfo;
import cn.zl.account.book.info.FundsOverviewInfo;
import cn.zl.account.book.info.FundsRecordTopInfo;
import cn.zl.account.book.info.FundsTrendInfo;
import cn.zl.account.book.view.response.FundsComposeResponse;
import cn.zl.account.book.view.response.FundsOverviewResponse;
import cn.zl.account.book.view.response.FundsRecordTopResponse;
import cn.zl.account.book.view.response.FundsTrendResponse;
import cn.zl.account.book.util.RmbUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
public class AnalyzeConverter {

    public static FundsOverviewResponse converterInfo2Resp(FundsOverviewInfo fundsOverviewInfo) {

        return FundsOverviewResponse.builder()
                .overview(constructOverview(fundsOverviewInfo.getOverview().getIncome(),
                        fundsOverviewInfo.getOverview().getExpenditure()))
                .yearOverview(constructOverview(fundsOverviewInfo.getYearOverview().getIncome(),
                        fundsOverviewInfo.getYearOverview().getExpenditure()))
                .monthOverview(constructOverview(fundsOverviewInfo.getMonthOverview().getIncome(),
                        fundsOverviewInfo.getMonthOverview().getExpenditure()))
                .weekOverview(constructOverview(fundsOverviewInfo.getWeekOverview().getIncome(),
                        fundsOverviewInfo.getWeekOverview().getExpenditure()))
                .assets(RmbUtils.convertFen2Yuan(fundsOverviewInfo.getAssets()))
                .liabilities(RmbUtils.convertFen2Yuan(fundsOverviewInfo.getLiabilities()))
                .build();
    }

    public static FundsTrendResponse converterInfo2Resp(FundsTrendInfo fundsTrendInfo) {
        return FundsTrendResponse.builder()
                .fundsRecordDate(fundsTrendInfo.getFundsRecordDate())
                .balance(fundsTrendInfo.getBalance())
                .fundsType(fundsTrendInfo.getFundsType())
                .build();
    }

    public static FundsComposeResponse converterInfo2Resp(FundsComposeInfo fundsComposeInfo) {
        return FundsComposeResponse.builder()
                .incomeCompose(constructCompose(fundsComposeInfo.getIncomeCompose()))
                .expenditureCompose(constructCompose(fundsComposeInfo.getExpenditureCompose()))
                .build();
    }

    public static FundsRecordTopResponse converterInfo2Resp(FundsRecordTopInfo fundsRecordTopInfo) {
        return FundsRecordTopResponse.builder()
                .incomeTops(constructTops(fundsRecordTopInfo.getIncomeTops()))
                .expenditureTops(constructTops(fundsRecordTopInfo.getExpenditureTops()))
                .build();
    }

    private static List<FundsRecordTopResponse.TopInfo> constructTops(List<FundsRecordTopInfo.TopInfo> topsInfo) {
        return topsInfo.stream()
                .map(top -> FundsRecordTopResponse.TopInfo
                        .builder()
                        .fundsRecordBalance(top.getFundsRecordBalance())
                        .fundsRecordDescribe(top.getFundsRecordDescribe())
                        .fundsRecordTime(top.getFundsRecordTime())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private static List<FundsComposeResponse.Compose> constructCompose(List<FundsComposeInfo.Compose> fundsComposes) {
        return fundsComposes.stream()
                .map(compose -> FundsComposeResponse.Compose
                        .builder()
                        .classifyName(compose.getClassifyName())
                        .classifyIds(compose.getClassifyIds())
                        .percent(compose.getPercent())
                        .build())
                .collect(Collectors.toList());
    }

    private static FundsOverviewResponse.Overview constructOverview(Long income, Long expenditure) {
        return new FundsOverviewResponse.Overview(income, expenditure);
    }

    private AnalyzeConverter() {
    }
}
