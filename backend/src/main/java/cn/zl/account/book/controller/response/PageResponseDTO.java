package cn.zl.account.book.controller.response;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponseDTO<T> extends ResponseDTO<T>{
    private Integer pageSize;

    private Integer pageNumber;

    private Long totalSize;

    private List<T> responseContent;

    public static <T> PageResponseDTO<T> wrapPageResponse(ResponseStatusEnum responseStatus, Page page,List<T> content){
        PageResponseDTO<T> response = new PageResponseDTO<>();
        response.setResponseCode(responseStatus.getResponseCode());
        response.setResponseMessage(responseStatus.getResponseMessage());

        response.setResponseContent(content);

        response.setPageSize(page.getSize());
        response.setPageNumber(page.getNumber());
        response.setTotalSize(page.getTotalElements());
        return  response;
    }
}
