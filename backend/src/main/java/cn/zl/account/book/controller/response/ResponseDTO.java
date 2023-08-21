package cn.zl.account.book.controller.response;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class ResponseDTO<T> {

    private Integer responseCode;

    private String responseMessage;

    private T responseContent;

    public ResponseDTO() {
    }

    public ResponseDTO(Integer responseCode, String responseMessage, T responseContent) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseContent = responseContent;
    }

    public static <T> ResponseDTO<T> wrapResponse(Integer responseCode, String responseMessage, T responseContent){
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setResponseCode(responseCode);
        response.setResponseMessage(responseMessage);
        response.setResponseContent(responseContent);
        return  response;
    }

    public static <T> ResponseDTO<T> wrapResponse(ResponseStatusEnum responseStatus, T responseContent){
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setResponseCode(responseStatus.getResponseCode());
        response.setResponseMessage(responseStatus.getResponseMessage());
        response.setResponseContent(responseContent);
        return  response;
    }
}
