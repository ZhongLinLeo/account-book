package cn.zl.account.book.domain.biz.user;

import cn.zl.account.book.application.domain.UserDomainService;
import cn.zl.account.book.application.info.UserInfo;
import cn.zl.account.book.domain.converter.UserEntityConverter;
import cn.zl.account.book.domain.utils.SnowIdUtil;
import cn.zl.account.book.infrastructure.biz.user.UserRepository;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Service
public class UserDomainServiceImpl implements UserDomainService {
    @Resource
    private UserRepository userRepository;

    @Override
    public void createUser(UserInfo userInfo) {
        UserEntity userEntity = UserEntityConverter.info2Entity(userInfo);

        long userId = SnowIdUtil.nextId();
        userEntity.setUserId(userId);

        LocalDateTime now = LocalDateTime.now();
        userEntity.setCreateTime(now);
        userEntity.setModifyTime(now);
        userEntity.setInvalid(0);

        userRepository.save(userEntity);
    }

    @Override
    public void modifyUser(UserInfo userInfo) {
        UserEntity userEntity = UserEntityConverter.info2Entity(userInfo);

        LocalDateTime now = LocalDateTime.now();
        userEntity.setModifyTime(now);
        userEntity.setInvalid(0);

        userRepository.save(userEntity);
    }

    @Override
    public void delUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
