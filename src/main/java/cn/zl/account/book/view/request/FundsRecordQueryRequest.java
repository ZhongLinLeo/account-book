package cn.zl.account.book.view.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 分类id列表
     */
    private List<Long> classifyIds;

    /**
     * 账户列表
     */
    private List<Long> accountIds;


    @Override
    public String getSortFiled() {
        return StringUtils.isBlank(sortFiled) ? "fundsRecordTime" : sortFiled;
    }

    public String getRecordKeyword() {
        return StringUtils.isBlank(recordKeyword) ? null : "%" + recordKeyword + "%";
    }
}
