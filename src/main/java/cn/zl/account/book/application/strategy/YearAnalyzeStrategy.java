package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.enums.TrendAnalyzeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin.zl
 */
@Slf4j
@Component
public class YearAnalyzeStrategy extends BaseAnalyzeStrategy {

    private static final String TYPE_FORMAT = "%Y-%m";

    @Override
    protected List<String> statisticPeriod() {
        int year = LocalDate.now().getYear();
        int yearOfMonth = 12;
        ArrayList<String> period = new ArrayList<>();
        for (int month = 1; month <= yearOfMonth; month++) {
            period.add(LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("yyyy-MM")));
        }
        return period;
    }

    @Override
    protected LocalDate startTime() {
        LocalDate today = LocalDate.now();
        return today.withDayOfYear(1);
    }

    @Override
    protected LocalDate endTime() {
        return startTime().plusYears(1);
    }

    @Override
    protected String statisticSqlFormat() {
        return TYPE_FORMAT;
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
