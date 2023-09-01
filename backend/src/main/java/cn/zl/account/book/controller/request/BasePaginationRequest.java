package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public abstract class BasePaginationRequest {

    Integer pageSize;

    Integer pageNumber;

}
