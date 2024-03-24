package cn.zl.account.book.application;

import cn.zl.account.book.info.BudgetDetailInfo;
import cn.zl.account.book.info.BudgetInfo;

/**
 * @author lin.zl
 */
public interface BudgetApplicationService {

    /**
     * 创建预算
     *
     * @param budgetInfo budgetInfo
     */
    void createBudget(BudgetInfo budgetInfo);

    /**
     * 修改预算
     *
     * @param budgetId budgetId
     */
    void removeBudget(Long budgetId);

    /**
     * 修改预算
     *
     * @param budgetInfo budgetInfo
     */
    void modifyBudget(BudgetInfo budgetInfo);

    /**
     * 查询预算
     *
     * @param budgetId budgetId
     * @return budgetInfo
     */
    BudgetDetailInfo findBudget(Long budgetId);

}
