package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Builder
@Data
public class AccountTransferInfo {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 目标账户ID
     */
    private Long targetAccountId;

    /**
     * 源账户ID
     */
    private Long sourceAccountId;

    /**
     * 转账金额
     */
    private Long transferBalance;

    private LocalDateTime operateTime;
}
