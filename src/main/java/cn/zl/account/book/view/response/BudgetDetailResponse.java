package cn.zl.account.book.view.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class BudgetDetailResponse {

    private Long budgetId;

    private String budgetName;

    private String budgetAmount;

    private String budgetDesc;

    private String budgetRemark;

    private LocalDateTime budgetStart;

}
