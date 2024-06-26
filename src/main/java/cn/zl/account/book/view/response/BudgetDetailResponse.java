package cn.zl.account.book.view.response;

import cn.zl.account.book.enums.BudgetTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class BudgetDetailResponse {

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
     *  预算结束时间
     */
    private LocalDateTime budgetEnd;

    /**
     * 预算类型
     */
    private BudgetTypeEnum budgetType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 已用金额
     */
    private Long usedAmount;

}
