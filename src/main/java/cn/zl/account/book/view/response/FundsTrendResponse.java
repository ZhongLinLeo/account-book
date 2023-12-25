package cn.zl.account.book.view.response;

import lombok.Builder;
import lombok.Data;

/**
 * 资金趋势
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
     * 金额
     */
    private Double balance;

    /**
     * 资金类型
     */
    private String fundsType;
}
