package cn.zl.account.book.view.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class AccountOperateRequest {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 目标账户ID
     */
    private Long targetAccountId;

    /**
     * 来源账户ID
     */
    private Long sourceAccountId;

    /**
     * 金额
     * 还款金额、转账金额
     */
    private Double balance;

    private LocalDateTime operateTime;
}
