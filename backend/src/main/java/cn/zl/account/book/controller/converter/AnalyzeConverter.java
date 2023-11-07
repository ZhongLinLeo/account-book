package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.response.FundsOverviewResponse;
import cn.zl.account.book.controller.response.FundsTrendResponse;
import cn.zl.account.book.controller.utils.RmbUtils;

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
                .income(fundsTrendInfo.getIncome())
                .expenditure(fundsTrendInfo.getExpenditure())
                .build();
    }

    private static FundsOverviewResponse.Overview constructOverview(Long income, Long expenditure) {
        return new FundsOverviewResponse.Overview(income, expenditure);
    }

    private AnalyzeConverter() {
    }
}
