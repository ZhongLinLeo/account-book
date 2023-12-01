package cn.zl.account.book.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsRecordTopResponse {

    private List<TopInfo> incomeTops;

    private List<TopInfo> expenditureTops;

    @Data
    @Builder
    public static class TopInfo {

        private Double fundsRecordBalance;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime fundsRecordTime;

        private String fundsRecordDescribe;
    }

}
