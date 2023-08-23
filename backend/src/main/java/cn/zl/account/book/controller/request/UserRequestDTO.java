package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class UserRequestDTO {

    private Long userId;

    private String userName;

    private String userRemark;

    private String userPassword;
}
