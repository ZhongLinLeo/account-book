package cn.zl.account.book.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordRequest {

    private Long fundsRecordBalance;

    private String fundsRecordRemark;

    private LocalDateTime fundsRecordTime;

    /**
     * fund code
     */
    private Long fundsRecordClassifyId;

    /**
     * account relation
     */
    private Long fundsAccountId;


    private String fundsRecordDescribe;

    private Long fundsUserId;
}
