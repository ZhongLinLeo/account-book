package cn.zl.account.book.domain.impl;

import cn.zl.account.book.domain.UserDomainService;
import cn.zl.account.book.info.UserInfo;
import cn.zl.account.book.domain.converter.UserEntityConverter;
import cn.zl.account.book.infrastructure.repository.UserRepository;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import cn.zl.account.book.util.SnowIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Service
@Slf4j
public class UserDomainServiceImpl implements UserDomainService {
    @Resource
    private UserRepository userRepository;

    @Override
    public void createUser(UserInfo userInfo) {
        UserEntity userEntity = UserEntityConverter.info2Entity(userInfo);

        long userId = SnowIdUtils.nextId();
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
