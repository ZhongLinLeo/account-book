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
public class PageBaseResponseDTO<T> extends BaseResponseDTO {
    private Integer pageSize;

    private Integer pageNumber;

    private Long totalSize;

    private List<T> responseContent;

    public static <T> PageBaseResponseDTO<T> wrapPageResponse(ResponseStatusEnum responseStatus,
                                                              Integer pageSize, Integer pageNumber,
                                                              Long totalSize, List<T> content) {
        PageBaseResponseDTO<T> response = new PageBaseResponseDTO<>();
        response.setResponseCode(responseStatus.getResponseCode());
        response.setResponseMessage(responseStatus.getResponseMessage());

        response.setResponseContent(content);
        response.setPageSize(pageSize);
        response.setPageNumber(pageNumber);
        response.setTotalSize(totalSize);
        return response;
    }
}
