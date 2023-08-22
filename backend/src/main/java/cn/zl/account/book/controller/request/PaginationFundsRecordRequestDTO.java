package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class PaginationFundsRecordRequestDTO {

    private Integer pageSize;

    private Integer pageNumber;

    private String recordKeyWord;

    private Integer fundsCode;

    private Long accountId;
}
