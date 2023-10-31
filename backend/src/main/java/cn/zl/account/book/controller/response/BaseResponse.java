package cn.zl.account.book.controller.response;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public abstract class BaseResponse {

    private Integer code;

    private String message;

    private Boolean success;
}
