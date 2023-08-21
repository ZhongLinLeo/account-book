package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.controller.response.AccountInfoResponseDTO;
import cn.zl.account.book.controller.request.AccountRequestDTO;

/**
 * 账户信息转换器
 *
 * @author lin.zl
 */
public final class AccountConverter {

    public static AccountInfo accountRequest2AccountInfo(AccountRequestDTO accountRequest) {
        return AccountInfo.builder()
                .accountName(accountRequest.getAccountName())
                .accountDescribe(accountRequest.getAccountDescribe())
                .accountBalance(accountRequest.getAccountBalance())
                .build();
    }


    public static AccountInfoResponseDTO accountInfo2AccountResp(AccountInfo accountInfo) {
        return AccountInfoResponseDTO.builder()
                .accountId(accountInfo.getAccountId())
                .accountName(accountInfo.getAccountName())
                .accountDescribe(accountInfo.getAccountDescribe())
                .accountBalance(accountInfo.getAccountBalance())
                .accountIncome(accountInfo.getAccountIncome())
                .accountExpenditure(accountInfo.getAccountExpenditure())
                .build();
    }

    private AccountConverter() {
    }
}
