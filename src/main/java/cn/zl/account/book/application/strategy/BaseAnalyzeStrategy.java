package cn.zl.account.book.application.strategy;

import cn.zl.account.book.enums.ClassifyTypeEnum;
import cn.zl.account.book.enums.TrendAnalyzeEnum;
import cn.zl.account.book.info.FundsComposeInfo;
import cn.zl.account.book.info.FundsRecordTopInfo;
import cn.zl.account.book.info.FundsTrendInfo;
import cn.zl.account.book.infrastructure.DO.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTrendBo;
import cn.zl.account.book.infrastructure.repository.ComplexAnalyzeRepository;
import cn.zl.account.book.infrastructure.repository.FundsRecordRepository;
import cn.zl.account.book.util.RmbUtils;
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

    @Resource
    private ComplexAnalyzeRepository complexAnalyzeRepository;

    /**
     * trend analyze
     *
     * @return trend analyze
     */
    public List<FundsTrendInfo> trendAnalyze() {
        String format = statisticFormat();
        List<AnalyzeTrendBo> analyzeTrendBos = complexAnalyzeRepository.queryFundsTrend(format, startTime(), endTime());

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
    protected abstract String statisticFormat();

    /**
     * funds compose
     *
     * @return funds compose
     */
    public FundsComposeInfo composeAnalyze() {
        List<AnalyzeComposeBo> incomeCompose = complexAnalyzeRepository
                .queryFundsCompose(ClassifyTypeEnum.INCOME.getClassifyType(), startTime(), endTime());

        List<AnalyzeComposeBo> expenditureCompose = complexAnalyzeRepository
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
        List<AnalyzeTopsBo> incomeTops = complexAnalyzeRepository
                .queryFundsTops(ClassifyTypeEnum.INCOME.getClassifyType(), startTime(), endTime());

        List<AnalyzeTopsBo> expenditureTops = complexAnalyzeRepository
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

        List<FundsComposeInfo.Compose> composeList = new ArrayList<>();

        composes = composes.stream()
                .sorted(Comparator.comparingLong(AnalyzeComposeBo::getTotalFundsBalance).reversed())
                .collect(Collectors.toList());

        final Set<String> classifyIds =
                composes.stream().map(AnalyzeComposeBo::getClassifyId)
                        .map(String::valueOf).collect(Collectors.toSet());

        long remains = totalBalance.longValue();
        for (AnalyzeComposeBo compose : composes) {
            final String classifyId = String.valueOf(compose.getClassifyId());
            FundsComposeInfo.Compose composeInfo = FundsComposeInfo.Compose.builder()
                    .percent(RmbUtils.convertFen2Yuan(compose.getTotalFundsBalance()))
                    .classifyName(compose.getClassifyName())
                    .classifyIds(Collections.singleton(classifyId))
                    .build();
            composeList.add(composeInfo);
            classifyIds.remove(classifyId);

            remains -= compose.getTotalFundsBalance();
            double remainsPercent = BigDecimal.valueOf(remains)
                    .divide(totalBalance, 2, RoundingMode.HALF_UP).doubleValue() * 100;
            if (0 < remainsPercent && remainsPercent < 5) {
                FundsComposeInfo.Compose remainsCompose = FundsComposeInfo.Compose.builder()
                        .percent(RmbUtils.convertFen2Yuan(remains))
                        .classifyName("其他")
                        .classifyIds(classifyIds)
                        .build();
                composeList.add(remainsCompose);
                break;
            }
        }

        return composeList;
    }
}
