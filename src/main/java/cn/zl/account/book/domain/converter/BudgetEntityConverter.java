package cn.zl.account.book.domain.converter;

import cn.zl.account.book.info.BudgetDetailInfo;
import cn.zl.account.book.info.BudgetInfo;
import cn.zl.account.book.infrastructure.entity.BudgetEntity;
import cn.zl.account.book.view.request.BudgetRequest;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
public class BudgetEntityConverter {

    public static BudgetEntity converter2Entity(BudgetInfo budgetInfo) {
        BudgetEntity budgetEntity = new BudgetEntity();

        budgetEntity.setBudgetName(budgetInfo.getBudgetName());
        budgetEntity.setBudgetAmount(budgetInfo.getBudgetAmount());
        budgetEntity.setBudgetType(budgetInfo.getBudgetType());
        budgetEntity.setBudgetDesc(budgetInfo.getBudgetDesc());
        budgetEntity.setBudgetStart(budgetInfo.getBudgetStart());
        budgetEntity.setBudgetEnd(budgetInfo.getBudgetEnd());

        return budgetEntity;
    }

    public static BudgetDetailInfo converter2DetailInfo(BudgetEntity budgetEntity) {
        BudgetDetailInfo budgetInfo = new BudgetDetailInfo();

        budgetInfo.setBudgetId(budgetEntity.getBudgetId());
        budgetInfo.setBudgetName(budgetEntity.getBudgetName());
        budgetInfo.setBudgetAmount(budgetEntity.getBudgetAmount());
        budgetInfo.setBudgetType(budgetEntity.getBudgetType());
        budgetInfo.setBudgetDesc(budgetEntity.getBudgetDesc());
        budgetInfo.setBudgetStart(budgetEntity.getBudgetStart());
        budgetInfo.setBudgetEnd(budgetEntity.getBudgetEnd());
        budgetInfo.setCreateTime(budgetEntity.getCreateTime());
        budgetInfo.setModifyTime(budgetEntity.getModifyTime());

        return budgetInfo;
    }


    private BudgetEntityConverter() {
        // none
    }
}

