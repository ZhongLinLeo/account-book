package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * account create
 *
 * @author create by leo.zl on 2023/8/17
 */
@Data
public class AccountRequestDTO {

    private String  accountName;

    private String  accountDescribe;

    private Long  accountBalance;

}
