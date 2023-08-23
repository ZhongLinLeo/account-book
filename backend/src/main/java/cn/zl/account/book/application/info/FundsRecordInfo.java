package cn.zl.account.book.application.info;

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

    private String fundsRecordClassifyName;

    private Long fundsAccountId;

    private Long fundsUserId;

    private Long fundsUserName;
}
