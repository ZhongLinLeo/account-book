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
    private Integer pageSize;

    private Integer pageNumber;

    private Long totalSize;

    private List<T> responseContent;

    public static <T> PageBaseResponse<T> wrapPageResponse(ResponseStatusEnum responseStatus,
                                                           Integer pageSize, Integer pageNumber,
                                                           Long totalSize, List<T> content) {
        PageBaseResponse<T> response = new PageBaseResponse<>();
        response.setResponseCode(responseStatus.getResponseCode());
        response.setResponseMessage(responseStatus.getResponseMessage());

        response.setResponseContent(content);
        response.setPageSize(pageSize);
        response.setPageNumber(pageNumber);
        response.setTotalSize(totalSize);
        return response;
    }

    public static <T> PageBaseResponse<T> wrapSuccessPageResponse(Integer pageSize, Integer pageNumber,
                                                                  Long totalSize, List<T> content) {
        return wrapPageResponse(ResponseStatusEnum.SUCCESS, pageSize, pageNumber, totalSize, content);
    }
}
