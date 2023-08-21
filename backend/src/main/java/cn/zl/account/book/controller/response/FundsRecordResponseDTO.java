package cn.zl.account.book.controller.response;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordResponseDTO {

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
