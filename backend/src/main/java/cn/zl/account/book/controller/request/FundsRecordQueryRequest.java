package cn.zl.account.book.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FundsRecordQueryRequest extends BasePaginationRequest {

    private String recordKeyword;

    private Long classifyId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long accountId;

    public String getRecordKeyword() {
        return StringUtils.isBlank(recordKeyword) ? null : "%" + recordKeyword + "%";
    }
}
