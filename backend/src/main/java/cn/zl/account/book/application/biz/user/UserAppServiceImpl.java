package cn.zl.account.book.application.biz.user;

import cn.zl.account.book.application.info.UserInfo;
import cn.zl.account.book.controller.application.UserAppService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lin.zl
 */
@Service
public class UserAppServiceImpl implements UserAppService {


    @Override
    public List<UserInfo> listUser() {
        return null;
    }

    @Override
    public void createUser(UserInfo userInfo) {

    }

    @Override
    public void modifyUser(UserInfo userInfo) {

    }

    @Override
    public void delUser(Long userId) {

    }
}
