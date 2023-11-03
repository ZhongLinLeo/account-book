package cn.zl.account.book.controller.converter;

import cn.zl.account.book.application.info.AccountInfo;
import cn.zl.account.book.application.info.AccountTransferInfo;
import cn.zl.account.book.controller.request.AccountRequest;
import cn.zl.account.book.controller.request.AccountTransferRequest;
import cn.zl.account.book.controller.response.AccountInfoResponse;

/**
 * 账户信息转换器
 *
 * @author lin.zl
 */
public final class AccountConverter {

    public static AccountInfo req2Info(AccountRequest accountRequest) {
        return AccountInfo.builder()
                .accountName(accountRequest.getAccountName())
                .accountDescribe(accountRequest.getAccountDescribe())
                .accountBalance(accountRequest.getAccountBalance())
                .accountIncome(accountRequest.getAccountIncome())
                .accountExpenditure(accountRequest.getAccountExpenditure())
                .accountOwnershipId(accountRequest.getAccountOwnershipId())
                .build();
    }

    public static AccountInfoResponse info2Resp(AccountInfo accountInfo) {
        return AccountInfoResponse.builder()
                .accountId(accountInfo.getAccountId())
                .accountName(accountInfo.getAccountName())
                .accountOwner(accountInfo.getAccountOwner())
                .accountOwnershipId(accountInfo.getAccountOwnershipId())
                .accountDescribe(accountInfo.getAccountDescribe())
                .accountBalance(accountInfo.getAccountBalance())
                .accountIncome(accountInfo.getAccountIncome())
                .accountExpenditure(accountInfo.getAccountExpenditure())
                .build();
    }

    public static AccountTransferInfo req2Info(AccountTransferRequest transferRequest) {
        return AccountTransferInfo.builder()
                .accountId(transferRequest.getAccountId())
                .targetAccountId(transferRequest.getTargetAccountId())
                .transferBalance(transferRequest.getTransferBalance())
                .sourceAccountId(transferRequest.getSourceAccountId())
                .build();
    }

    private AccountConverter() {
    }
}
