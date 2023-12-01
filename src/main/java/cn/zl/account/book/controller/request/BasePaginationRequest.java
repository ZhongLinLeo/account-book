package cn.zl.account.book.controller.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * @author lin.zl
 */
@Data
public abstract class BasePaginationRequest {

    @NotNull(message = "pageSize 不能为空")
    protected Integer pageSize;

    @NotNull(message = "pageNumber 不能为空")
    protected Integer current;

    protected String sortFiled;

    protected String order;

    public String getSortFiled() {
        return StringUtils.isBlank(sortFiled) ? "create_time" : sortFiled;
    }

    public String getOrder() {
        return StringUtils.isBlank(order) ? "DESC": order;
    }

    public Integer getCurrent() {
        return current - 1;
    }
}
