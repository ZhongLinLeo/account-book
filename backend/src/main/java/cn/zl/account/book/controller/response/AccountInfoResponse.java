package cn.zl.account.book.controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

/**
 * account view info
 *
 * @author create by leo.zl on 2023/8/17
 */
@Builder
@Data
public class AccountInfoResponse {

    @JsonSerialize(using= ToStringSerializer.class)
    private Long accountId;

    private String accountName;

    private String accountDescribe;

    private String accountOwner;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long accountOwnershipId;

    private Long accountBalance;

    private Long accountIncome;

    private Long accountExpenditure;

    private Integer accountType;
}
