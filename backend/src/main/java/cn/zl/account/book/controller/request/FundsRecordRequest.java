package cn.zl.account.book.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Double fundsRecordBalance;

    private String fundsRecordRemark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fundsRecordTime;

    /**
     * fund code
     */
    private Long fundsRecordClassifyId;

    /**
     * account relation
     */
    private Long fundsRecordAccountId;

    @NotBlank(message = "描述不能为空")
    private String fundsRecordDescribe;

    private Long fundsUserId;
}
