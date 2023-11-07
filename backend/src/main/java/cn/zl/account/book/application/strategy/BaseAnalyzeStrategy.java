package cn.zl.account.book.application.strategy;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lin.zl
 */
@Component
public abstract class BaseAnalyzeStrategy implements InitializingBean {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    /**
     * trend analyze
     *
     * @return trend analyze
     */
    abstract List<FundsTrendInfo> trendAnalyze();

    /**
     * strategy mark
     *
     * @return strategy name
     */
    public abstract TrendAnalyzeEnum strategyName();
}
