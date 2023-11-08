package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.info.FundsComposeInfo;
import cn.zl.account.book.application.info.FundsRecordTopInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.bo.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTopsBo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Component
public abstract class BaseAnalyzeStrategy implements InitializingBean {

    @Resource
    protected FundsRecordRepository fundsRecordRepository;

    /**
     * trend analyze
     *
     * @return trend analyze
     */
    public abstract List<FundsTrendInfo> trendAnalyze();

    /**
     * strategy mark
     *
     * @return strategy name
     */
    public abstract TrendAnalyzeEnum strategyName();

    /**
     * funds compose
     *
     * @return funds compose
     */
    public abstract FundsComposeInfo composeAnalyze();

    /**
     * funds tops
     *
     * @return funds tops
     */
    public abstract FundsRecordTopInfo fundsTops();

    protected List<FundsRecordTopInfo.TopInfo> constructTops(List<AnalyzeTopsBo> tops) {
        if (CollectionUtils.isEmpty(tops)) {
            return Collections.emptyList();
        }
        return tops.stream().map(bo -> FundsRecordTopInfo.TopInfo
                .builder()
                .fundsRecordBalance(bo.getFundsRecordBalance())
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

        BigDecimal totalBalance = BigDecimal.valueOf(composes.stream().mapToLong(AnalyzeComposeBo::getTotalFundsBalance).sum());
        return composes.stream().map(compose -> {
            double percent = BigDecimal.valueOf(compose.getTotalFundsBalance())
                    .divide(totalBalance, 2, RoundingMode.HALF_UP).doubleValue();

            return FundsComposeInfo.Compose.builder()
                    .percent(percent)
                    .classifyName(compose.getClassifyName())
                    .build();
        }).collect(Collectors.toList());
    }



}
