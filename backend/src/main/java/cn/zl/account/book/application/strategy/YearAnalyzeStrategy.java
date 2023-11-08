package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.application.info.FundsComposeInfo;
import cn.zl.account.book.application.info.FundsRecordTopInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.controller.utils.RmbUtils;
import cn.zl.account.book.infrastructure.bo.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTrendBo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Slf4j
public class YearAnalyzeStrategy extends BaseAnalyzeStrategy {

    private static final String TYPE_FORMAT = "%Y-%m";

    @Override
    public List<FundsTrendInfo> trendAnalyze() {

        LocalDate today = LocalDate.now();
        LocalDate startTime = today.withDayOfYear(1);
        LocalDate endTime = startTime.plusYears(1);

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
                    String recordDate = period.format(DateTimeFormatter.ofPattern("yyyy-MM"));
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
        int year = LocalDate.now().getYear();
        int yearOfMonth = 12;
        ArrayList<LocalDate> period = new ArrayList<>();
        for (int month = 1; month <= yearOfMonth; month++) {
            period.add(LocalDate.of(year, month, 1));
        }
        return period;
    }

    @Override
    public TrendAnalyzeEnum strategyName() {
        return TrendAnalyzeEnum.YEAR;
    }

    @Override
    public FundsComposeInfo composeAnalyze() {
        LocalDate today = LocalDate.now();
        LocalDate startTime = today.withDayOfYear(1);
        LocalDate endTime = startTime.plusYears(1);

        List<AnalyzeComposeBo> incomeCompose = fundsRecordRepository
                .queryFundsCompose(ClassifyTypeEnum.INCOME.getClassifyType(), startTime, endTime);

        List<AnalyzeComposeBo> expenditureCompose = fundsRecordRepository
                .queryFundsCompose(ClassifyTypeEnum.EXPENDITURE.getClassifyType(), startTime, endTime);

        return FundsComposeInfo.builder()
                .incomeCompose(constructCompose(incomeCompose))
                .expenditureCompose(constructCompose(expenditureCompose))
                .build();
    }

    @Override
    public FundsRecordTopInfo fundsTops() {
        LocalDate today = LocalDate.now();
        LocalDate startTime = today.withDayOfYear(1);
        LocalDate endTime = startTime.plusYears(1);

        List<AnalyzeTopsBo> incomeTops = fundsRecordRepository
                .queryFundsTops(ClassifyTypeEnum.INCOME.getClassifyType(), startTime, endTime);


        List<AnalyzeTopsBo> expenditureTops = fundsRecordRepository
                .queryFundsTops(ClassifyTypeEnum.EXPENDITURE.getClassifyType(), startTime, endTime);

        return FundsRecordTopInfo.builder()
                .incomeTops(constructTops(incomeTops))
                .expenditureTops(constructTops(expenditureTops))
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AnalyzeServiceFactory.storeAnalyzeStrategy(this);
    }
}
