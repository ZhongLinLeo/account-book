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
