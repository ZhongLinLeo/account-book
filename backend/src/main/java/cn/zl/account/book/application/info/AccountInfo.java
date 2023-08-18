package cn.zl.account.book.application.info;

import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class AccountInfo {

    private Long  accountId;

    private String  accountName;

    private String  accountDescribe;

    private Long  accountBalance;

    private Long  accountIncome;

    private Long  accountExpenditure;
}
