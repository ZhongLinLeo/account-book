package cn.zl.account.book.controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordResponse {

    @JsonSerialize(using= ToStringSerializer.class)
    private Long fundsRecordId;

    private Long fundsRecordBalance;

    private LocalDateTime fundsRecordTime;

    private String fundsRecordDescribe;

    private String fundsRecordRemark;

    private FundsRecordClassifyResponse classifyInfo;

    private AccountInfoResponse accountInfo;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long fundsUserId;
}
