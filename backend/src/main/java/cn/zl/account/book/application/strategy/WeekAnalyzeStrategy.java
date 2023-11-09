package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.application.info.FundsRecordTopInfo;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.infrastructure.bo.AnalyzeTopsBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
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
public class WeekAnalyzeStrategy extends BaseAnalyzeStrategy {

    private static final String TYPE_FORMAT = "%Y-%m-%d";

    @Override
    protected List<String> statisticPeriod() {
        int weekOfDay = 7;
        ArrayList<String> period = new ArrayList<>();
        LocalDate periodDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int day = 1; day <= weekOfDay; day++) {
            period.add(periodDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            periodDate = periodDate.plusDays(1);
        }
        return period;
    }

    @Override
    protected LocalDate startTime() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    @Override
    protected LocalDate endTime() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }

    @Override
    protected String statisticSqlFormat() {
        return TYPE_FORMAT;
    }

    @Override
    public TrendAnalyzeEnum strategyName() {
        return TrendAnalyzeEnum.WEEK;
    }

    @Override
    public void afterPropertiesSet() {
        AnalyzeServiceFactory.storeAnalyzeStrategy(this);
    }
}
