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
public class PageFundsComposeRequest extends BasePaginationRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private List<Long> classifyIds;

    /**
     * 根据金额倒序排列
     *
     * @return
     */
    @Override
    public String getSortFiled() {
        return StringUtils.isBlank(sortFiled) ? "fundsRecordBalance" : sortFiled;
    }
}
