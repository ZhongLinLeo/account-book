package cn.zl.account.book.controller.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class UserResponseDTO {

    private Long userId;

    private String userName;

    private String userRemark;
}
