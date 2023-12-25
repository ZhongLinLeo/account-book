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
public class FundsRecordTopInfo {

    private List<TopInfo> incomeTops;

    private List<TopInfo> expenditureTops;

    @Data
    @Builder
    public static class TopInfo {

        private String classifyName;

        private Double fundsRecordBalance;

        private LocalDateTime fundsRecordTime;

        private String fundsRecordDescribe;
    }

}
