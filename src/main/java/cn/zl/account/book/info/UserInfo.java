package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class UserInfo {

    private Long userId;

    private String userName;

    private String userRemark;

    private String userPassword;
}
