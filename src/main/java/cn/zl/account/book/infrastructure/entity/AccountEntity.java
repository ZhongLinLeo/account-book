package cn.zl.account.book.infrastructure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account")
public class AccountEntity extends EntityBase{

    @Id
    private Long accountId;

    /**
     * 账户名称
     */
    private String  accountName;

    /**
     * 账户描述
     */
    private String  accountDescribe;

    /**
     * 账户所属
     */
    private Long  accountOwnershipId;

    /**
     * 账户余额
     */
    private Long  accountBalance;

    /**
     * 账户收入
     */
    private Long  accountIncome;

    /**
     * 账户支出
     */
    private Long  accountExpenditure;

    /**
     * 卡类型
     */
    private Integer accountType;

    /**
     * 卡排序
     */
    private Integer accountSort;

    /**
     * 卡是否可用
     */
    private Integer accountAvailable;

    /**
     * 还款时间
     */
    private String repayDate;
}
