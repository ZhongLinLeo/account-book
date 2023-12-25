package cn.zl.account.book.enums;

/**
 * @author lin.zl
 */
public enum DefaultClassifyEnum {

    /**
     * 转出
     */
    TRANSFER_OUT("转出", 1000000000000000001L),

    /**
     * 转入
     */
    TRANSFER_IN("转入", 1000000000000000002L),

    /**
     * 还款转出
     */
    REPAYMENT_OUT("还款转出", 1000000000000000003L),

    /**
     * 还款转入
     */
    REPAYMENT_IN("还款转入", 1000000000000000004L),
    ;

    private final String classifyName;


    private final Long classifyId;

    public String getClassifyName() {
        return classifyName;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    DefaultClassifyEnum(String classifyName, Long classifyId) {
        this.classifyName = classifyName;
        this.classifyId = classifyId;
    }
}
