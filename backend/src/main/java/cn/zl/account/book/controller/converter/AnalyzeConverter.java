package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.controller.response.FundsOverviewResponse;

/**
 * @author lin.zl
 */
public class AnalyzeConverter {

    public static FundsOverviewResponse converterInfo2Resp(FundsOverviewInfo fundsOverviewInfo) {

        return FundsOverviewResponse.builder()
                .overview(FundsOverviewResponse.Overview
                        .builder()
                        .income(fundsOverviewInfo.getOverview().getIncome())
                        .expenditure(fundsOverviewInfo.getOverview().getExpenditure())
                        .build())
                .yearOverview(FundsOverviewResponse.Overview
                        .builder()
                        .income(fundsOverviewInfo.getYearOverview().getIncome())
                        .expenditure(fundsOverviewInfo.getYearOverview().getExpenditure())
                        .build())
                .monthOverview(FundsOverviewResponse.Overview
                        .builder()
                        .income(fundsOverviewInfo.getMonthOverview().getIncome())
                        .expenditure(fundsOverviewInfo.getMonthOverview().getExpenditure())
                        .build())
                .weekOverview(FundsOverviewResponse.Overview
                        .builder()
                        .income(fundsOverviewInfo.getWeekOverview().getIncome())
                        .expenditure(fundsOverviewInfo.getWeekOverview().getExpenditure())
                        .build())
                .assets(fundsOverviewInfo.getAssets())
                .liabilities(fundsOverviewInfo.getLiabilities())
                .build();
    }


    private AnalyzeConverter() {
    }
}
