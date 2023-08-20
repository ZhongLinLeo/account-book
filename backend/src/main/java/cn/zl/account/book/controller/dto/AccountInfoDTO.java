package cn.zl.account.book.controller.dto;

import lombok.Builder;
import lombok.Data;

/**
 * account view info
 *
 * @author create by leo.zl on 2023/8/17
 */
@Builder
@Data
public class AccountInfoDTO {

    private Long  accountId;

    private String  accountName;

    private String  accountDescribe;

    private Long  accountBalance;

    private Long  accountIncome;

    private Long  accountExpenditure;
}
