package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
