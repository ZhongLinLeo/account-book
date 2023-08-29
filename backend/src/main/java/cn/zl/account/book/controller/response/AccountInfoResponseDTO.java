package cn.zl.account.book.controller.response;

import lombok.Builder;
import lombok.Data;

/**
 * account view info
 *
 * @author create by leo.zl on 2023/8/17
 */
@Builder
@Data
public class AccountInfoResponseDTO {

    private Long accountId;

    private String accountName;

    private String accountDescribe;

    private String accountOwner;

    private Long accountBalance;

    private Long accountIncome;

    private Long accountExpenditure;
}
