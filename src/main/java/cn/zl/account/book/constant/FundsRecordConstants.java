package cn.zl.account.book.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lin.zl
 */
public final class FundsRecordConstants {

    public static final String BALANCE_FIELD = "金额";
    public static final String RECORD_TIME_FIELD = "时间";
    public static final String DESCRIBE_FIELD = "描述";
    public static final String REMARK_FIELD = "备注";
    public static final String CLASSIFY_NAME_FIELD = "分类";
    public static final String USER_NAME_FIELD = "使用人";

    private static final Map<String, Integer> EXCEL_FIELD_MAP = new HashMap<>(8);

    static {
        EXCEL_FIELD_MAP.put(BALANCE_FIELD, -1);
        EXCEL_FIELD_MAP.put(RECORD_TIME_FIELD, -1);
        EXCEL_FIELD_MAP.put(DESCRIBE_FIELD, -1);
        EXCEL_FIELD_MAP.put(REMARK_FIELD, -1);
        EXCEL_FIELD_MAP.put(CLASSIFY_NAME_FIELD, -1);
        EXCEL_FIELD_MAP.put(USER_NAME_FIELD, -1);
    }

    public static void putValue(String field, Integer cellNum) {
        Set<String> strings = EXCEL_FIELD_MAP.keySet();
        if (strings.contains(field)) {
            EXCEL_FIELD_MAP.put(field, cellNum);
        }
    }

    public static Integer getValue(String field) {
        return EXCEL_FIELD_MAP.get(field);
    }

    private FundsRecordConstants() {
    }
}
