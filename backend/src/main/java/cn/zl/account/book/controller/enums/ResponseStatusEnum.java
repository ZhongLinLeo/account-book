package cn.zl.account.book.controller.enums;

import lombok.Getter;

/**
 * account book error , begin with 95, and the number 2 mean success, 3 mean client error, 4 mean server error
 *
 * @author lin.zl
 */
@Getter
public  enum ResponseStatusEnum {

    /**
     * success
     */
    SUCCESS(952001,"success"),
    ;


    private final Integer responseCode;

    private final String responseMessage;

    ResponseStatusEnum(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
