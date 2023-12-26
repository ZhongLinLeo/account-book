package cn.zl.account.book.converter;

import cn.zl.account.book.info.AccountInfo;
import cn.zl.account.book.info.AccountTransferInfo;
import cn.zl.account.book.view.request.AccountRequest;
import cn.zl.account.book.view.request.AccountOperateRequest;
import cn.zl.account.book.view.response.AccountInfoResponse;
import cn.zl.account.book.util.RmbUtils;

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
                .accountBalance(RmbUtils.convertYuan2Fen(accountRequest.getAccountBalance()))
                .accountIncome(RmbUtils.convertYuan2Fen(accountRequest.getAccountIncome()))
                .accountExpenditure(RmbUtils.convertYuan2Fen(accountRequest.getAccountExpenditure()))
                .accountOwnershipId(accountRequest.getAccountOwnershipId())
                .accountType(accountRequest.getAccountType())
                .build();
    }

    public static AccountInfoResponse info2Resp(AccountInfo accountInfo) {
        return AccountInfoResponse.builder()
                .accountId(accountInfo.getAccountId())
                .accountName(accountInfo.getAccountName())
                .accountOwner(accountInfo.getAccountOwner())
                .accountOwnershipId(accountInfo.getAccountOwnershipId())
                .accountDescribe(accountInfo.getAccountDescribe())
                .accountBalance(RmbUtils.convertFen2Yuan(accountInfo.getAccountBalance()))
                .accountIncome(RmbUtils.convertFen2Yuan(accountInfo.getAccountIncome()))
                .accountExpenditure(RmbUtils.convertFen2Yuan(accountInfo.getAccountExpenditure()))
                .accountType(accountInfo.getAccountType())
                .repayDate(accountInfo.getRepayDate())
                .build();
    }

    public static AccountTransferInfo req2Info(AccountOperateRequest operateRequest) {
        return AccountTransferInfo.builder()
                .accountId(operateRequest.getAccountId())
                .targetAccountId(operateRequest.getTargetAccountId())
                .transferBalance(RmbUtils.convertYuan2Fen(operateRequest.getBalance()))
                .sourceAccountId(operateRequest.getSourceAccountId())
                .operateTime(operateRequest.getOperateTime())
                .build();
    }

    private AccountConverter() {
    }
}
