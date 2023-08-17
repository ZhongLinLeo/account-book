package cn.leo.zl.ui.dto.request;

import lombok.Data;

/**
 * account create
 *
 * @author create by leo.zl on 2023/8/17
 */
@Data
public class AccountReqDTO {

    private String  accountName;

    private String  accountDescribe;

    private Long  accountBalance;

}
