package cn.leo.zl.ui.dto.response;

import lombok.Data;

/**
 * account view info
 *
 * @author create by leo.zl on 2023/8/17
 */
@Data
public class AccountRespDTO {

    private Long  accountId;

    private String  accountName;

    private String  accountDescribe;

    private Long  accountBalance;

    private Long  accountIncome;

    private Long  accountExpenditure;
}
