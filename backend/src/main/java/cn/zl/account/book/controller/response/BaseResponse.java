package cn.zl.account.book.controller.response;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public abstract class BaseResponse {

    private Integer responseCode;

    private String responseMessage;
}
