package cn.zl.account.book.view.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class UserResponse {

    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    private String userName;

    private String userRemark;
}
