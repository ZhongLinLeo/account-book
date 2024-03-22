package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.infrastructure.entity.RelBudgetClassifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface RelBudgetClassifyRepository extends JpaRepository<RelBudgetClassifyEntity, Long>,
        JpaSpecificationExecutor<RelBudgetClassifyEntity> {


    /**
     * remove all classify relation by budget id
     *
     * @param budgetId budgetId
     */
    void removeRelBudgetClassifyEntitiesByBudgetId(Long budgetId);
}
