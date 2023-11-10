package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.info.FundsComposeInfo;
import cn.zl.account.book.application.info.FundsRecordTopInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.controller.utils.RmbUtils;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.bo.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTrendBo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
public abstract class BaseAnalyzeStrategy implements InitializingBean {

    @Resource
    protected FundsRecordRepository fundsRecordRepository;

    /**
     * trend analyze
     *
     * @return trend analyze
     */
    public List<FundsTrendInfo> trendAnalyze() {

        List<AnalyzeTrendBo> analyzeTrendBos = fundsRecordRepository.queryFundsTrend(statisticSqlFormat(),
                startTime(), endTime());

        Map<String, AnalyzeTrendBo> incomeMap = analyzeTrendBos.stream()
                .filter(trend -> Objects.equals(ClassifyTypeEnum.INCOME.getClassifyType(), trend.getClassifyType()))
                .collect(Collectors.toMap(AnalyzeTrendBo::getFundsRecordDate, e -> e, (o, n) -> n));

        Map<String, AnalyzeTrendBo> expenditureMap = analyzeTrendBos.stream()
                .filter(trend -> Objects.equals(ClassifyTypeEnum.EXPENDITURE.getClassifyType(),
                        trend.getClassifyType()))
                .collect(Collectors.toMap(AnalyzeTrendBo::getFundsRecordDate, e -> e, (o, n) -> n));

        final List<FundsTrendInfo> fundsTrendInfos = new ArrayList<>();
        for (String period : statisticPeriod()) {
            AnalyzeTrendBo incomeTrend = incomeMap.get(period);
            final FundsTrendInfo incomeTrendInfo = constructAnalyzeTrend(period, incomeTrend, ClassifyTypeEnum.INCOME);

            AnalyzeTrendBo expenditureTrend = expenditureMap.get(period);
            final FundsTrendInfo expenditureTrendInfo = constructAnalyzeTrend(period, expenditureTrend,
                    ClassifyTypeEnum.EXPENDITURE);

            fundsTrendInfos.add(incomeTrendInfo);
            fundsTrendInfos.add(expenditureTrendInfo);
        }
        return fundsTrendInfos;
    }

    private FundsTrendInfo constructAnalyzeTrend(String period, AnalyzeTrendBo trendBo,
                                                 ClassifyTypeEnum classifyTypeEnum) {
        return FundsTrendInfo.builder()
                .fundsRecordDate(period)
                .fundsType(classifyTypeEnum.getClassifyTypeName())
                .balance(Objects.isNull(trendBo) ? 0 : RmbUtils.convertFen2Yuan(trendBo.getTotalFundsBalance()))
                .build();

    }

    /**
     * strategy mark
     *
     * @return strategy name
     */
    public abstract TrendAnalyzeEnum strategyName();

    /**
     * statistic period
     *
     * @return statistic list
     */
    protected abstract List<String> statisticPeriod();

    /**
     * start time
     *
     * @return start time
     */
    protected abstract LocalDate startTime();

    /**
     * end time
     *
     * @return end time
     */
    protected abstract LocalDate endTime();

    /**
     * format
     *
     * @return statistic format
     */
    protected abstract String statisticSqlFormat();

    /**
     * funds compose
     *
     * @return funds compose
     */
    public FundsComposeInfo composeAnalyze() {
        List<AnalyzeComposeBo> incomeCompose = fundsRecordRepository
                .queryFundsCompose(ClassifyTypeEnum.INCOME.getClassifyType(), startTime(), endTime());

        List<AnalyzeComposeBo> expenditureCompose = fundsRecordRepository
                .queryFundsCompose(ClassifyTypeEnum.EXPENDITURE.getClassifyType(), startTime(), endTime());

        return FundsComposeInfo.builder()
                .incomeCompose(constructCompose(incomeCompose))
                .expenditureCompose(constructCompose(expenditureCompose))
                .build();
    }

    /**
     * funds tops
     *
     * @return funds tops
     */
    public FundsRecordTopInfo fundsTops() {
        List<AnalyzeTopsBo> incomeTops = fundsRecordRepository
                .queryFundsTops(ClassifyTypeEnum.INCOME.getClassifyType(), startTime(), endTime());

        List<AnalyzeTopsBo> expenditureTops = fundsRecordRepository
                .queryFundsTops(ClassifyTypeEnum.EXPENDITURE.getClassifyType(), startTime(), endTime());

        return FundsRecordTopInfo.builder()
                .incomeTops(constructTops(incomeTops))
                .expenditureTops(constructTops(expenditureTops))
                .build();
    }

    protected List<FundsRecordTopInfo.TopInfo> constructTops(List<AnalyzeTopsBo> tops) {
        if (CollectionUtils.isEmpty(tops)) {
            return Collections.emptyList();
        }
        return tops.stream()
                .map(bo -> FundsRecordTopInfo.TopInfo
                        .builder()
                        .fundsRecordBalance(RmbUtils.convertFen2Yuan(bo.getFundsRecordBalance()))
                        .fundsRecordTime(bo.getFundsRecordTime())
                        .classifyName(bo.getClassifyName())
                        .fundsRecordDescribe(bo.getFundsRecordDesc())
                        .build()
                ).collect(Collectors.toList());
    }

    protected List<FundsComposeInfo.Compose> constructCompose(List<AnalyzeComposeBo> composes) {
        if (CollectionUtils.isEmpty(composes)) {
            return Collections.emptyList();
        }

        BigDecimal totalBalance =
                BigDecimal.valueOf(composes.stream().mapToLong(AnalyzeComposeBo::getTotalFundsBalance).sum());
        return composes.stream()
                .map(compose -> {
                    double percent = BigDecimal.valueOf(compose.getTotalFundsBalance())
                            .divide(totalBalance, 2, RoundingMode.HALF_UP).doubleValue();

                    return FundsComposeInfo.Compose.builder()
                            .percent(percent * 100)
                            .classifyName(compose.getClassifyName())
                            .build();
                }).collect(Collectors.toList());
    }
}
