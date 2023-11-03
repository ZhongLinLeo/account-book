package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class AccountTransferRequest {

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
     * 转账金额
     */
    private Long transferBalance;
}
