package cn.zl.account.book.controller.enums;

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
    CLASSIFY_EXIST(953001, "该分类已存在"),
    CLASSIFY_NONE_EXIST(953002, "该分类不存在"),
    CLASSIFY_USING(953003, "分类已被使用，无法删除"),


    REQUEST_PARAM_ERROR(953004, "参数错误:{0}"),

    FUNDS_RECORD_NONE_EXIST(953005, "该记录不存在"),
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
