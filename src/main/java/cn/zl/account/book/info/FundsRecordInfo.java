package cn.zl.account.book.info;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordInfo {

    private Long fundsRecordId;

    private Long fundsRecordBalance;

    private LocalDateTime fundsRecordTime;

    private String fundsRecordDescribe;

    private String fundsRecordRemark;

    private Long fundsRecordClassifyId;

    private Long fundsAccountId;

    private Long fundsUserId;
}
