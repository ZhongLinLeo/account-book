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
@Table(name = "funds_record_classify")
public class FundsRecordClassifyEntity extends EntityBase{

    @Id
    private Long classifyId;

    private String classifyName;

    /**
     * 分类类型
     * 0:支出
     * 1:收入
     */
    private Integer classifyType;

    private String classifyDescribe;

}
