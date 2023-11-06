package cn.zl.account.book.application.enums;

/**
 * @author lin.zl
 */
public enum DefaultClassifyEnum {

    /**
     * 转出
     */
    TRANSFER_OUT("转出", 10000000000001L),

    TRANSFER_IN("转入", 10000000000002L),
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
