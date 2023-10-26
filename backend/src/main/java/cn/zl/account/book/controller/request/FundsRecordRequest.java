package cn.zl.account.book.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordRequest {

    @NotNull(message = "金额不能为空")
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

    @NotBlank(message = "描述不能为空")
    private String fundsRecordDescribe;

    private Long fundsUserId;
}
