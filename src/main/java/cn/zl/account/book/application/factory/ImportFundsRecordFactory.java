package cn.zl.account.book.application.factory;

import cn.zl.account.book.application.strategy.BaseFundsRecordImportStrategy;
import cn.zl.account.book.enums.FundsRecordImportTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lin.zl
 */
public class ImportFundsRecordFactory {


    private static final Map<FundsRecordImportTypeEnum, BaseFundsRecordImportStrategy> STRATEGY_MAP = new ConcurrentHashMap<>();

    public static void storeImportStrategy(BaseFundsRecordImportStrategy importStrategy) {
        STRATEGY_MAP.put(importStrategy.strategyName(), importStrategy);
    }

    public static BaseFundsRecordImportStrategy matchImportStrategy(FundsRecordImportTypeEnum trendAnalyzeEnum) {
        return STRATEGY_MAP.get(trendAnalyzeEnum);
    }
}
