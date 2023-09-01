package cn.zl.account.book.controller.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordResponse {

    private Long fundsRecordBalance;

    private LocalDateTime fundsRecordTime;

    private String fundsRecordDescribe;

    private String fundsRecordRemark;

    private Long fundsRecordClassifyId;

    private Long fundsAccountId;

    private Long fundsUserId;
}
