package cn.zl.account.book.controller.application;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.info.FundsComposeInfo;
import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.application.info.FundsRecordTopInfo;
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
    List<FundsTrendInfo> fundsTrend(TrendAnalyzeEnum trendType);

    /**
     * funds compose
     *
     * @param trendType trend type
     * @return funds compose
     */
    FundsComposeInfo fundsCompose(TrendAnalyzeEnum trendType);

    /**
     * tops funds record
     *
     * @param trendType trend type
     * @return tops
     */
    FundsRecordTopInfo fundsTops(TrendAnalyzeEnum trendType);

}
