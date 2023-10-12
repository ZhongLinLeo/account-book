package cn.zl.account.book.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FundsClassifyQueryRequest extends BasePaginationRequest{

    /**
     * 名称模糊搜索
     */
    private String fundsClassifyNameKeyword;

    private Integer fundsClassifyType;
}
