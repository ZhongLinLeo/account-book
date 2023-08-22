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
@Table(name = "funds_record")
public class FundsRecordEntity extends EntityBase{

    @Id
    private Long fundsRecordId;

    private Long balance;

    private String fundsRemark;

    private String fundsTime;

    private Integer fundsCode;

    private Long accountId;
}
