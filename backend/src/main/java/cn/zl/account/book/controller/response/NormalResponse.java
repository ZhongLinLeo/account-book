package cn.zl.account.book.controller.response;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NormalResponse<T> extends BaseResponse {

    private T responseContent;

    public NormalResponse() {
    }

    public static <T> NormalResponse<T> wrapResponse(Integer responseCode, String responseMessage, T responseContent){
        NormalResponse<T> response = new NormalResponse<>();
        response.setResponseCode(responseCode);
        response.setResponseMessage(responseMessage);
        response.setResponseContent(responseContent);
        return  response;
    }

    public static <T> NormalResponse<T> wrapResponse(ResponseStatusEnum responseStatus, T responseContent){
        NormalResponse<T> response = new NormalResponse<>();
        response.setResponseCode(responseStatus.getResponseCode());
        response.setResponseMessage(responseStatus.getResponseMessage());
        response.setResponseContent(responseContent);
        return  response;
    }

    public static <T> NormalResponse<T> wrapSuccessResponse(T responseContent){
        return  wrapResponse(ResponseStatusEnum.SUCCESS,responseContent);
    }
}
