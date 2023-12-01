package cn.zl.account.book.domain.converter;

import cn.zl.account.book.application.info.UserInfo;
import cn.zl.account.book.infrastructure.entity.UserEntity;

/**
 * @author lin.zl
 */
public final class UserEntityConverter {

    public static UserInfo entity2Info(UserEntity entity) {
        return UserInfo.builder()
                .userId(entity.getUserId())
                .userName(entity.getUserName())
                .userPassword(entity.getUserPassword())
                .userRemark(entity.getUserRemark())
                .build();
    }

    public static UserEntity info2Entity(UserInfo info) {
        UserEntity entity = new UserEntity();
        entity.setUserId(info.getUserId());
        entity.setUserName(info.getUserName());
        entity.setUserPassword(info.getUserPassword());
        entity.setUserRemark(entity.getUserRemark());
        return entity;
    }

    private UserEntityConverter() {
    }
}
