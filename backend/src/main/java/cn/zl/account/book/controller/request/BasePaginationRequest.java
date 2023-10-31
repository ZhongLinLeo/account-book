package cn.zl.account.book.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lin.zl
 */
@Data
public abstract class BasePaginationRequest {

    @NotNull(message = "pageSize 不能为空")
    private Integer pageSize;

    @NotNull(message = "pageNumber 不能为空")
    private Integer current;

    public Integer getCurrent() {
        return current - 1;
    }
}
