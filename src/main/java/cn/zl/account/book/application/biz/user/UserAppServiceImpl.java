package cn.zl.account.book.application.biz.user;

import cn.zl.account.book.application.domain.UserDomainService;
import cn.zl.account.book.application.info.UserInfo;
import cn.zl.account.book.controller.application.UserAppService;
import cn.zl.account.book.domain.converter.UserEntityConverter;
import cn.zl.account.book.infrastructure.biz.user.UserRepository;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Service
public class UserAppServiceImpl implements UserAppService {

    @Resource
    private UserDomainService userDomainService;

    @Resource
    private UserRepository userRepository;

    @Override
    public List<UserInfo> listUser() {
        List<UserEntity> entities = userRepository.findAll();
        return entities.stream().map(UserEntityConverter::entity2Info).collect(Collectors.toList());
    }

    @Override
    public void createUser(UserInfo userInfo) {
        userDomainService.createUser(userInfo);
    }

    @Override
    public void modifyUser(UserInfo userInfo) {
        userDomainService.modifyUser(userInfo);
    }

    @Override
    public void delUser(Long userId) {
        userDomainService.delUser(userId);
    }
}
