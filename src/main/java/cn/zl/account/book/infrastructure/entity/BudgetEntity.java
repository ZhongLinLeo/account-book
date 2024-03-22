package cn.zl.account.book.infrastructure.entity;

import cn.zl.account.book.enums.BudgetTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "budget")
public class BudgetEntity extends EntityBase{

    @Id
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


}
