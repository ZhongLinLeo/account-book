package cn.zl.account.book.controller.utils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author lin.zl
 */
public class RmbUtils {

    public static long convertYuan2Fen(Double yuanAmount) {
        if (yuanAmount.isNaN()) {
            return 0L;
        }

        return BigDecimal.valueOf(yuanAmount).multiply(BigDecimal.valueOf(100)).longValue();
    }

    public static double convertFen2Yuan(Long fenAmount) {
        if (Objects.isNull(fenAmount)) {
            return 0L;
        }

        return BigDecimal.valueOf(fenAmount).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_CEILING).doubleValue();
    }

    private RmbUtils() {
    }
}
