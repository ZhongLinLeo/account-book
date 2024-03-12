package cn.zl.account.book.converter;

import cn.zl.account.book.info.BudgetInfo;
import cn.zl.account.book.view.request.BudgetRequest;
import cn.zl.account.book.view.response.BudgetDetailResponse;

/**
 * @author lin.zl
 */
public class BudgetConverter {

    public static BudgetInfo convertRequest2Info(BudgetRequest budgetRequest) {
        BudgetInfo budgetInfo = new BudgetInfo();

        budgetInfo.setBudgetId(budgetRequest.getBudgetId());
        budgetInfo.setBudgetDesc(budgetRequest.getBudgetDesc());
        budgetInfo.setBudgetEnd(budgetRequest.getBudgetEnd());
        budgetInfo.setBudgetName(budgetRequest.getBudgetName());
        budgetInfo.setBudgetRemark(budgetRequest.getBudgetRemark());
        budgetInfo.setBudgetStart(budgetRequest.getBudgetStart());
        budgetInfo.setBudgetType(budgetRequest.getBudgetType());
        budgetInfo.setBudgetAmount(budgetRequest.getBudgetAmount());

        return budgetInfo;
    }

    private BudgetConverter() {
        //  none
    }

    public static BudgetDetailResponse convertInfo2Response(BudgetInfo budgetInfo) {

        BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();

        budgetDetailResponse.setBudgetId(budgetInfo.getBudgetId());
        budgetDetailResponse.setBudgetDesc(budgetInfo.getBudgetDesc());
        budgetDetailResponse.setBudgetEnd(budgetInfo.getBudgetEnd());
        budgetDetailResponse.setBudgetName(budgetInfo.getBudgetName());
        budgetDetailResponse.setBudgetRemark(budgetInfo.getBudgetRemark());
        budgetDetailResponse.setBudgetStart(budgetInfo.getBudgetStart());

        return budgetDetailResponse;
    }
}
