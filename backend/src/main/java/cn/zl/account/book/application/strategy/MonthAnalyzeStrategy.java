package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.controller.utils.RmbUtils;
import cn.zl.account.book.infrastructure.bo.AnalyzeTrendBo;
import lombok.extern.slf4j.Slf4j;

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
public class MonthAnalyzeStrategy extends BaseAnalyzeStrategy {

    private static final String TYPE_FORMAT = "%Y-%m-%d";

    @Override
    public List<FundsTrendInfo> trendAnalyze() {

        LocalDate today = LocalDate.now();
        LocalDate startTime = today.withDayOfMonth(1);
        LocalDate endTime = today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

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
        int lengthOfMonth = LocalDate.now().lengthOfMonth();
        ArrayList<LocalDate> period = new ArrayList<>();
        for (int day = 1; day <= lengthOfMonth; day++) {
            period.add(LocalDate.now().withDayOfMonth(day));
        }
        return period;
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
