package cn.zl.account.book.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FundsRecordQueryRequest extends BasePaginationRequest {

    private String recordKeyword;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Long accountId;

    private Long classifyId;

    @Override
    public String getSortFiled() {
        return StringUtils.isBlank(sortFiled) ? "funds_record_time" : sortFiled;
    }

    public String getRecordKeyword() {
        return StringUtils.isBlank(recordKeyword) ? null : "%" + recordKeyword + "%";
    }
}
