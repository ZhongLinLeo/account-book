package cn.zl.account.book.controller.request;

import lombok.Data;

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
}
