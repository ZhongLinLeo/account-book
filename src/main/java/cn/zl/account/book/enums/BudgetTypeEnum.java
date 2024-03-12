package cn.zl.account.book.enums;

/**
 * @author lin.zl
 */
public enum BudgetTypeEnum {

    // 预算类型
    REPEAT("周期性"),
    LONELY("一次性"),
    ;

    private String name;

    BudgetTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
