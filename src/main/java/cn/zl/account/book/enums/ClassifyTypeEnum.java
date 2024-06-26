package cn.zl.account.book.enums;

import java.util.Objects;

/**
 * 分类枚举值
 *
 * @author lin.zl
 */
public enum ClassifyTypeEnum {

    /**
     * 支出
     */
    EXPENDITURE(0, "支出"),

    /**
     * 收入
     */
    INCOME(1, "收入"),

    ;

    private final Integer classifyType;
    private final String classifyTypeName;

    public Integer getClassifyType() {
        return classifyType;
    }

    public String getClassifyTypeName() {
        return classifyTypeName;
    }

    ClassifyTypeEnum(Integer classifyType, String classifyTypeName) {
        this.classifyType = classifyType;
        this.classifyTypeName = classifyTypeName;
    }

    public static String findClassifyTypeName(Integer classifyType) {
        if (Objects.equals(classifyType, EXPENDITURE.classifyType)) {
            return EXPENDITURE.getClassifyTypeName();
        } else {
            return INCOME.getClassifyTypeName();
        }
    }
    public static ClassifyTypeEnum findClassifyTypeEnum(Integer classifyType) {
        if (Objects.equals(classifyType, EXPENDITURE.classifyType)) {
            return EXPENDITURE;
        } else {
            return INCOME;
        }
    }
}
