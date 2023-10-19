package cn.zl.account.book.controller.enums;

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
    OUTCOME(0, "支出"),

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
        if (Objects.equals(classifyType, OUTCOME.classifyType)) {
            return OUTCOME.getClassifyTypeName();
        } else {
            return INCOME.getClassifyTypeName();
        }
    }
}
