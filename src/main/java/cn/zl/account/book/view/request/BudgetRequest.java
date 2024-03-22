package cn.zl.account.book.view.request;

import cn.zl.account.book.enums.BudgetTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author lin.zl
 */
@Data
public class BudgetRequest {

    /**
     * 预算ID
     */
    private Long budgetId;

    /**
     * 预算名称
     */
    private String budgetName;

    /**
     * 预算金额
     */
    private String budgetAmount;

    /**
     * 预算描述
     */
    private String budgetDesc;

    /**
     * 预算开始时间
     */
    private LocalDateTime budgetStart;

    /**
     * 预算结束时间
     */
    private LocalDateTime budgetEnd;

    /**
     * 预算类型
     */
    private BudgetTypeEnum budgetType;

    /**
     * 分类ID
     */
    private Set<Long> classifyIds;
}
