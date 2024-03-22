package cn.zl.account.book.domain;

import cn.zl.account.book.info.BudgetInfo;
import cn.zl.account.book.infrastructure.entity.BudgetEntity;

/**
 *
 * @author lin.zl
 */
public interface BudgetDomainService {

    /**
     * 创建预算
     * @param budgetEntity budget entity
     */
    void createBudget(BudgetEntity budgetEntity);

    /**
     * 删除预算
     * @param budgetEntity budget entity
     */
    void removeBudget(BudgetEntity budgetEntity);

    /**
     * 修改预算
     * @param budgetEntity budget entity
     */
    void modifyBudget(BudgetEntity budgetEntity);
}
