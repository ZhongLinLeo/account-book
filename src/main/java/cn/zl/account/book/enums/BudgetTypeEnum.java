package cn.zl.account.book.enums;

/**
 * @author lin.zl
 */
public enum BudgetTypeEnum {

    // 预算类型
    REPEAT(1, "周期性"),

    LONELY(2, "一次性"),
    ;

    private Integer type;
    private String typeName;

    BudgetTypeEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public String getName() {
        return typeName;
    }
}
