package cn.zl.account.book.view.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageFundsComposeRequest extends BasePaginationRequest {

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
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
        return StringUtils.isBlank(sortFiled) ? "funds_record_balance" : sortFiled;
    }
}
