package cn.zl.account.book.view.response;

import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.architecture.BizException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NormalResponse<T> extends BaseResponse {

    private T data;

    public NormalResponse() {
        // none
    }

    public static <T> NormalResponse<T> wrapResponse(Integer responseCode, String responseMessage, T responseContent) {
        NormalResponse<T> response = new NormalResponse<>();
        response.setCode(responseCode);
        response.setMessage(responseMessage);
        response.setData(responseContent);
        return response;
    }

    public static <T> NormalResponse<T> wrapResponse(Integer responseCode, String responseMessage) {
        NormalResponse<T> response = new NormalResponse<>();
        response.setCode(responseCode);
        response.setMessage(responseMessage);
        return response;
    }

    public static <T> NormalResponse<T> wrapBizExceptionResponse(BizException bizException, T responseContent) {
        NormalResponse<T> response = new NormalResponse<>();
        response.setCode(bizException.getMessageCode());
        response.setMessage(bizException.getMessage());
        response.setData(responseContent);
        return response;
    }

    public static <T> NormalResponse<T> wrapResponse(ResponseStatusEnum responseStatus, T responseContent) {
        NormalResponse<T> response = new NormalResponse<>();
        response.setCode(responseStatus.getResponseCode());
        response.setMessage(responseStatus.getResponseMessage());
        response.setData(responseContent);
        return response;
    }

    public static <T> NormalResponse<T> wrapSuccessResponse(T responseContent) {
        final NormalResponse<T> response = wrapResponse(ResponseStatusEnum.SUCCESS, responseContent);
        response.setSuccess(Boolean.TRUE);

        return response;
    }
}
