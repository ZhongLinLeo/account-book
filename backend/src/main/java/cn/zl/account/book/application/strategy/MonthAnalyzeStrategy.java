package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin.zl
 */
@Slf4j
@Component
public class MonthAnalyzeStrategy extends BaseAnalyzeStrategy {

    private static final String TYPE_FORMAT = "%Y-%m-%d";

    @Override
    protected List<String> statisticPeriod() {
        int lengthOfMonth = LocalDate.now().lengthOfMonth();
        ArrayList<String> period = new ArrayList<>();
        for (int day = 1; day <= lengthOfMonth; day++) {
            period.add(LocalDate.now().withDayOfMonth(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return period;
    }

    @Override
    protected LocalDate startTime() {
        LocalDate today = LocalDate.now();
        return today.withDayOfMonth(1);
    }

    @Override
    protected LocalDate endTime() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
    }

    @Override
    protected String statisticSqlFormat() {
        return TYPE_FORMAT;
    }

    @Override
    public TrendAnalyzeEnum strategyName() {
        return TrendAnalyzeEnum.MONTH;
    }

    @Override
    public void afterPropertiesSet() {
        AnalyzeServiceFactory.storeAnalyzeStrategy(this);
    }
}
