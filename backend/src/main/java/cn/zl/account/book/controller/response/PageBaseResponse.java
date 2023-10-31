package cn.zl.account.book.controller.response;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageBaseResponse<T> extends BaseResponse {

    private Boolean success;

    private Long total;

    private List<T> data;

    public static <T> PageBaseResponse<T> wrapPageResponse(ResponseStatusEnum responseStatus,
                                                           Long totalSize, List<T> content) {
        PageBaseResponse<T> response = new PageBaseResponse<>();
        response.setCode(responseStatus.getResponseCode());
        response.setMessage(responseStatus.getResponseMessage());

        response.setData(content);
        response.setTotal(totalSize);
        return response;
    }

    public static <T> PageBaseResponse<T> wrapSuccessPageResponse(Long totalSize, List<T> content) {
        final PageBaseResponse<T> response = wrapPageResponse(ResponseStatusEnum.SUCCESS, totalSize, content);

        response.setSuccess(Boolean.TRUE);

        return response;
    }
}
