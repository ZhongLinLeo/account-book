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
@Table(name = "account")
public class AccountEntity extends EntityBase{

    @Id
    private Long accountId;

    private String  accountName;

    private String  accountDescribe;

    private Long  accountOwnershipId;

    private Long  accountBalance;

    private Long  accountIncome;

    private Long  accountExpenditure;
}
