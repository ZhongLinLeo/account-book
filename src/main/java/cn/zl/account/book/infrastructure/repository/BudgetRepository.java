package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.infrastructure.entity.AccountEntity;
import cn.zl.account.book.infrastructure.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long>, JpaSpecificationExecutor<BudgetEntity> {
}
