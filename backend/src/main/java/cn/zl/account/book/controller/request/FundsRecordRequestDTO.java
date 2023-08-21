package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordRequestDTO {

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
