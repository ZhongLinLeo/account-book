package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.request.AccountRequest;

/**
 * 账户信息转换器
 *
 * @author lin.zl
 */
public final class AccountConverter {


    public AccountInfo accountRequest2AccountInfo(AccountRequest accountRequest) {
        return AccountInfo.builder()
                .accountName(accountRequest.getAccountName())
                .accountDescribe(accountRequest.getAccountDescribe())
                .accountBalance(accountRequest.getAccountBalance())
                .build();
    }


    private AccountConverter() {
    }
}
