package cn.zl.account.book.controller.response;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lin.zl
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NormalResponseDTO<T> extends BaseResponseDTO{

    private T responseContent;

    public NormalResponseDTO() {
    }

    public static <T> NormalResponseDTO<T> wrapResponse(Integer responseCode, String responseMessage, T responseContent){
        NormalResponseDTO<T> response = new NormalResponseDTO<>();
        response.setResponseCode(responseCode);
        response.setResponseMessage(responseMessage);
        response.setResponseContent(responseContent);
        return  response;
    }

    public static <T> NormalResponseDTO<T> wrapResponse(ResponseStatusEnum responseStatus, T responseContent){
        NormalResponseDTO<T> response = new NormalResponseDTO<>();
        response.setResponseCode(responseStatus.getResponseCode());
        response.setResponseMessage(responseStatus.getResponseMessage());
        response.setResponseContent(responseContent);
        return  response;
    }

    public static <T> NormalResponseDTO<T> wrapSuccessResponse(T responseContent){
        return  wrapResponse(ResponseStatusEnum.SUCCESS,responseContent);
    }
}
