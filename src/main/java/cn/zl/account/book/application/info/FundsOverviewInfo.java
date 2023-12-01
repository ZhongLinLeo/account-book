package cn.zl.account.book.application.info;

import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsOverviewInfo {

    /**
     * 总概览
     */
    private Overview overview;

    /**
     * 年度概览
     */
    private Overview yearOverview;

    /**
     * 月度概览
     */
    private Overview monthOverview;

    /**
     * 周概览
     */
    private Overview weekOverview;

    /**
     * 资产
     */
    private Long assets;

    /**
     * 负债
     */
    private Long liabilities;

    @Data
    @Builder
    public static class Overview {

        private Long income;

        private Long expenditure;
    }

}
