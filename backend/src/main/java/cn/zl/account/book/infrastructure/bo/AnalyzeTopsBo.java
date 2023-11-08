package cn.zl.account.book.infrastructure.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
public class AnalyzeTopsBo {

    private String classifyName;

    private Long fundsRecordBalance;

    private String fundsRecordDesc;

    private LocalDateTime fundsRecordTime;

}
