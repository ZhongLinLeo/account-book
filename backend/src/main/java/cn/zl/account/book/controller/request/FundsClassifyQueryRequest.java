package cn.zl.account.book.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FundsClassifyQueryRequest extends BasePaginationRequest {

    /**
     * 名称模糊搜索
     */
    private String fundsClassifyNameKeyword;

    private Integer fundsClassifyType;

    public String getFundsClassifyNameKeyword() {
        return StringUtils.isBlank(fundsClassifyNameKeyword) ? null : "%" + fundsClassifyNameKeyword + "%";
    }
}
