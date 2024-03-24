package cn.zl.account.book.infrastructure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "rel_budget_classify")
public class RelBudgetClassifyEntity extends EntityBase{

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 预算id
     */
    private Long budgetId;

    /**
     * 分类id
     */
    private Long classifyId;

}
