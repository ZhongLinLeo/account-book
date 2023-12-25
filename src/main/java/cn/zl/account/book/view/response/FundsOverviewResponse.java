package cn.zl.account.book.view.response;

import cn.zl.account.book.util.RmbUtils;
import lombok.Builder;
import lombok.Data;

/**
 * 资金概览
 *
 * @author lin.zl
 */
@Data
@Builder
public class FundsOverviewResponse {

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
    private Double assets;

    /**
     * 负债
     */
    private Double liabilities;

    @Data
    public static class Overview {

        private Double income;

        private Double expenditure;

        public Overview(Long income, Long expenditure) {
            this.income = RmbUtils.convertFen2Yuan(income);
            this.expenditure = RmbUtils.convertFen2Yuan(expenditure);
        }
    }

}
