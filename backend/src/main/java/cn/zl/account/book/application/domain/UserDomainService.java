package cn.zl.account.book.application.domain;

import cn.zl.account.book.application.info.UserInfo;

/**
 * @author lin.zl
 */
public interface UserDomainService {

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
