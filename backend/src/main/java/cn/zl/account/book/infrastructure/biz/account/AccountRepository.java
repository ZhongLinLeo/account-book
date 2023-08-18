package cn.zl.account.book.infrastructure.biz.account;

import cn.zl.account.book.infrastructure.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * account repository
 *
 * @author lin.zl
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
