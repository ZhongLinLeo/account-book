package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author lin.zl
 */
@Data
@Builder
public class AccountInfo {

    private Long accountId;

    private String accountName;

    private String accountDescribe;

    private Long accountOwnershipId;

    private String accountOwner;

    private Long accountBalance;

    private Long accountIncome;

    private Long accountExpenditure;

    private Integer accountType;

    /**
     * 还款时间
     */
    private String repayDate;

    /**
     * 卡是否可用
     */
    private Integer accountAvailable;
}
