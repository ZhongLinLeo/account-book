package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsRecordSearchInfo {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<Long> classifyIds;

}
