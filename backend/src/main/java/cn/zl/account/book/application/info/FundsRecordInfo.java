package cn.zl.account.book.application.info;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordInfo {

    private Long fundsRecordId;

    private Long balance;

    private String fundsRemark;

    private String fundsTime;

    /**
     * fund code
     */
    private Integer fundsCode;

    /**
     * account relation
     */
    private Long accountId;
}
