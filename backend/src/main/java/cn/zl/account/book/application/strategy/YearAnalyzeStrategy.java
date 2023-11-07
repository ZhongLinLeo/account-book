package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.application.info.FundsTrendInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lin.zl
 */
@Slf4j
public class YearAnalyzeStrategy extends BaseAnalyzeStrategy {

    @Override
    public List<FundsTrendInfo> trendAnalyze() {
        LocalDate today = LocalDate.now();
        LocalDate firstDatOfYear = today.withDayOfYear(1);






        return null;
    }

    @Override
    public TrendAnalyzeEnum strategyName() {
        return TrendAnalyzeEnum.YEAR;
    }

    @Override
    public void afterPropertiesSet() {
        AnalyzeServiceFactory.storeAnalyzeStrategy(this);
    }
}
