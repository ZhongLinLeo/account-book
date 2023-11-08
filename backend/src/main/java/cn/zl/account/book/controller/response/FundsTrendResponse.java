package cn.zl.account.book.controller.response;

import lombok.Builder;
import lombok.Data;

/**
 * 资金概览
 *
 * @author lin.zl
 */
@Data
@Builder
public class FundsTrendResponse {

    /**
     * 资金记录时间
     */
    private String fundsRecordDate;

    /**
     * 收入
     */
    private Double income;

    /**
     * 支出
     */
    private Double expenditure;
}
