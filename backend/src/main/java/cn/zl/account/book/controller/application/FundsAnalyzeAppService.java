package cn.zl.account.book.controller.application;

import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;

import java.util.List;

/**
 * @author lin.zl
 */
public interface FundsAnalyzeAppService {

    /**
     * funds overview
     *
     * @return funds overview
     */
    FundsOverviewInfo fundsOverview();


    /**
     * funds trend
     *
     * @param trendType trend type
     * @return funds trend
     */
    List<FundsTrendInfo> fundsTrend(String trendType);

}
