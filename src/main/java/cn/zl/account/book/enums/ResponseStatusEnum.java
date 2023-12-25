package cn.zl.account.book.enums;

import lombok.Getter;

/**
 * account book error , begin with 95, and the number 2 mean success, 3 mean client error, 4 mean server error
 *
 * @author lin.zl
 */
@Getter
public enum ResponseStatusEnum {

    /**
     * success
     */
    SUCCESS(952001, "success"),

    /**
     * client error
     */
    REQUEST_PARAM_ERROR(953004, "参数错误:{0}"),

    CLASSIFY_EXIST(953101, "该分类已存在"),
    CLASSIFY_NONE_EXIST(953102, "该分类不存在"),
    CLASSIFY_USING(953103, "分类已被使用，无法删除"),



    FUNDS_RECORD_NONE_EXIST(953205, "该记录不存在"),

    ACCOUNT_NONE_EXIST(953306, "账户不存在"),
    ACCOUNT_BALANCE_NOT_ENOUGH(953307, "账户余额不足"),

    USER_NONE_EXIST(953407, "用户不存在"),

    /**
     * server error
     */
    SYSTEM_ERROR(954001, "system error"),
    ;


    private final Integer responseCode;

    private final String responseMessage;

    ResponseStatusEnum(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
