package cn.zl.account.book.controller.response;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public abstract class BaseResponseDTO {

    private Integer responseCode;

    private String responseMessage;
}
