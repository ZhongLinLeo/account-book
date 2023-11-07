package cn.zl.account.book.application.factory;

import cn.zl.account.book.application.enums.TrendAnalyzeEnum;
import cn.zl.account.book.application.strategy.BaseAnalyzeStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lin.zl
 */
public class AnalyzeServiceFactory {

    private static final Map<TrendAnalyzeEnum, BaseAnalyzeStrategy> ANALYZE_STRATEGY_MAP = new ConcurrentHashMap<>();

    public static void storeAnalyzeStrategy(BaseAnalyzeStrategy analyzeStrategy) {
        ANALYZE_STRATEGY_MAP.put(analyzeStrategy.strategyName(), analyzeStrategy);
    }

    public static BaseAnalyzeStrategy matchAnalyzeStrategy(TrendAnalyzeEnum trendAnalyzeEnum) {
        return ANALYZE_STRATEGY_MAP.get(trendAnalyzeEnum);
    }
}
