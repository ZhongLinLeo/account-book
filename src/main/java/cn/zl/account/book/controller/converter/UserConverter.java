package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.UserInfo;
import cn.zl.account.book.controller.request.UserRequest;
import cn.zl.account.book.controller.response.UserResponse;

/**
 * @author lin.zl
 */
public final class UserConverter {

    public static UserResponse info2Resp(UserInfo userInfo) {
        return UserResponse.builder()
                .userId(userInfo.getUserId())
                .userName(userInfo.getUserName())
                .userRemark(userInfo.getUserRemark())
                .build();
    }

    public static UserInfo req2Info(UserRequest req) {
        return UserInfo.builder()
                .userId(req.getUserId())
                .userName(req.getUserName())
                .userRemark(req.getUserRemark())
                .userPassword(req.getUserPassword())
                .build();
    }

    private UserConverter() {
    }
}
