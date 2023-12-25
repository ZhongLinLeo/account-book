package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

/**
 * 资金概览
 *
 * @author lin.zl
 */
@Data
@Builder
public class FundsTrendInfo {

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
