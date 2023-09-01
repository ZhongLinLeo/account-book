package cn.zl.account.book.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FundsRecordQueryRequest extends BasePaginationRequest {

    private String recordKeyWord;

    private Integer fundsCode;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long accountId;
}
