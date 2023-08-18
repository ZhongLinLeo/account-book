package cn.zl.account.book.infrastructure.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lin.zl
 */
@Data
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    private Long accountId;

}
