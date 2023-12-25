package cn.zl.account.book.enums;

/**
 * @author lin.zl
 */
public enum AccountTypeEnum {

    /**
     * 储蓄账户
     */
    DEBIT_ACCOUNT("储蓄账户", 0),

    /**
     * 信用卡
     */
    CREDIT_ACCOUNT("信用账户", 1),
    ;

    private final String accountTypeName;


    private final Integer accountType;

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public Integer getAccountType() {
        return accountType;
    }

    AccountTypeEnum(String classifyName, Integer accountType) {
        this.accountTypeName = classifyName;
        this.accountType = accountType;
    }
}
