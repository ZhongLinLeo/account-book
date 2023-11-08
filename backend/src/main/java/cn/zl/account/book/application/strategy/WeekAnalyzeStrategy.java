package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.controller.utils.RmbUtils;
import cn.zl.account.book.infrastructure.bo.AnalyzeTrendBo;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Slf4j
public class WeekAnalyzeStrategy extends BaseAnalyzeStrategy {

    private static final String TYPE_FORMAT = "%Y-%m-%d";

    @Override
    public List<FundsTrendInfo> trendAnalyze() {

        LocalDate today = LocalDate.now();
        LocalDate startTime = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endTime = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        List<AnalyzeTrendBo> analyzeTrendBos = fundsRecordRepository.queryFundsTrend(TYPE_FORMAT, startTime, endTime);

        Map<LocalDate, AnalyzeTrendBo> incomeMap = analyzeTrendBos.stream()
                .filter(trend -> Objects.equals(ClassifyTypeEnum.INCOME.getClassifyType(), trend.getClassifyType()))
                .collect(Collectors.toMap(AnalyzeTrendBo::getFundsRecordDate, e -> e, (o, n) -> n));

        Map<LocalDate, AnalyzeTrendBo> expenditureMap = analyzeTrendBos.stream()
                .filter(trend -> Objects.equals(ClassifyTypeEnum.EXPENDITURE.getClassifyType(), trend.getClassifyType()))
                .collect(Collectors.toMap(AnalyzeTrendBo::getFundsRecordDate, e -> e, (o, n) -> n));

        List<LocalDate> periods = statisticPeriod();

        return periods.stream()
                .map(period -> {
                    String recordDate = period.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    FundsTrendInfo.FundsTrendInfoBuilder builder = FundsTrendInfo.builder()
                            .fundsRecordDate(recordDate);

                    AnalyzeTrendBo incomeTrend = incomeMap.get(period);
                    if (Objects.nonNull(incomeTrend)) {
                        builder.income(RmbUtils.convertFen2Yuan(incomeTrend.getTotalFundsBalance()));
                    }
                    AnalyzeTrendBo expenditureTrend = expenditureMap.get(period);

                    if (Objects.nonNull(expenditureTrend)) {
                        builder.expenditure(RmbUtils.convertFen2Yuan(expenditureTrend.getTotalFundsBalance()));
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    private List<LocalDate> statisticPeriod() {
        int weekOfDay = 7;
        ArrayList<LocalDate> period = new ArrayList<>();
        LocalDate periodDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int day = 1; day <= weekOfDay; day++) {
            period.add(periodDate);
            periodDate = periodDate.plusDays(1);
        }
        return period;
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