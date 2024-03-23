package cn.zl.account.book.converter;

import cn.zl.account.book.info.BudgetDetailInfo;
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
        budgetInfo.setBudgetStart(budgetRequest.getBudgetStart());
        budgetInfo.setBudgetType(budgetRequest.getBudgetType());
        budgetInfo.setBudgetAmount(budgetRequest.getBudgetAmount());
        budgetInfo.setClassifyIds(budgetInfo.getClassifyIds());

        return budgetInfo;
    }

    private BudgetConverter() {
        //  none
    }

    public static BudgetDetailResponse convertInfo2Response(BudgetDetailInfo budgetDetailInfo) {

        BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();

        budgetDetailResponse.setBudgetId(budgetDetailInfo.getBudgetId());
        budgetDetailResponse.setBudgetAmount(budgetDetailInfo.getBudgetAmount());
        budgetDetailResponse.setBudgetDesc(budgetDetailInfo.getBudgetDesc());
        budgetDetailResponse.setBudgetEnd(budgetDetailInfo.getBudgetEnd());
        budgetDetailResponse.setBudgetName(budgetDetailInfo.getBudgetName());
        budgetDetailResponse.setBudgetStart(budgetDetailInfo.getBudgetStart());
        budgetDetailResponse.setBudgetType(budgetDetailInfo.getBudgetType());
        budgetDetailResponse.setCreateTime(budgetDetailInfo.getCreateTime());
        budgetDetailResponse.setModifyTime(budgetDetailInfo.getModifyTime());
        budgetDetailResponse.setUsedAmount(budgetDetailInfo.getUsedAmount());

        return budgetDetailResponse;
    }
}
