package cn.zl.account.book.application;

import cn.zl.account.book.info.UserInfo;

import java.util.List;

/**
 * @author lin.zl
 */
public interface UserAppService {

    /**
     * list user
     *
     * @return users
     */
    List<UserInfo> listUser();

    /**
     * create use
     *
     * @param userInfo user info
     */
    void createUser(UserInfo userInfo);

    /**
     * modify use
     *
     * @param userInfo user info
     */
    void modifyUser(UserInfo userInfo);

    /**
     * del user
     *
     * @param userId user id
     */
    void delUser(Long userId);
}
