package cn.zl.account.book.infrastructure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "funds_record")
public class FundsRecordEntity extends EntityBase{

    public static final String FUNDS_RECORD_TIME = "fundsRecordTime";
    public static final String CLASSIFY_TYPE = "classifyType";

    @Id
    private Long fundsRecordId;

    private Long fundsRecordBalance;

    private LocalDateTime fundsRecordTime;

    private String fundsRecordDescribe;

    private String fundsRecordRemark;

    @JoinColumn()
    private Long fundsRecordClassifyId;

    private Long fundsAccountId;

    private Long fundsUserId;
}
