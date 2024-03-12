package cn.zl.account.book.controller;

import cn.zl.account.book.application.BudgetApplicationService;
import cn.zl.account.book.converter.BudgetConverter;
import cn.zl.account.book.info.BudgetInfo;
import cn.zl.account.book.view.request.BudgetRequest;
import cn.zl.account.book.view.response.BudgetDetailResponse;
import cn.zl.account.book.view.response.NormalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lin.zl
 */
@Controller
@Slf4j
@RequestMapping("budget")
@CrossOrigin
public class BudgetController {

    @Resource
    private BudgetApplicationService budgetApplicationService;

    /**
     * 创建预算
     *
     * @param budgetRequest budgetRequest info
     * @return true if success
     */
    @PostMapping
    public NormalResponse<Boolean> createBudget(BudgetRequest budgetRequest) {
        BudgetInfo budgetInfo = BudgetConverter.convertRequest2Info(budgetRequest);

        budgetApplicationService.createBudget(budgetInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    /**
     * 删除预算
     *
     * @param budgetId budgetId
     * @return true if success
     */
    @DeleteMapping({"{budgetId}"})
    public NormalResponse<Boolean> removeBudget(@PathVariable Long budgetId) {
        budgetApplicationService.removeBudget(budgetId);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @PutMapping
    public NormalResponse<Boolean> modifyBudget(BudgetRequest budgetRequest) {
        BudgetInfo budgetInfo = BudgetConverter.convertRequest2Info(budgetRequest);
        budgetApplicationService.modifyBudget(budgetInfo);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @GetMapping({"{budgetId}"})
    public NormalResponse<BudgetDetailResponse> findBudget(@PathVariable Long budgetId) {
        BudgetInfo budgetInfo = budgetApplicationService.findBudget(budgetId);

        BudgetDetailResponse budgetDetailResponse = BudgetConverter.convertInfo2Response(budgetInfo);

        return NormalResponse.wrapSuccessResponse(budgetDetailResponse);
    }

}
