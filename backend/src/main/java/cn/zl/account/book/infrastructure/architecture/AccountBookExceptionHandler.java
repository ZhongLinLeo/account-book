package cn.zl.account.book.infrastructure.architecture;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.response.NormalResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author create by leo.zl on 2023/10/22
 */
@RestControllerAdvice
public class AccountBookExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public NormalResponse bizExceptionHandler(BizException bizException) {
        return NormalResponse.wrapBizExceptionResponse(bizException);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public NormalResponse exceptionHandler(RuntimeException exception) {
        return NormalResponse.wrapResponse(ResponseStatusEnum.SYSTEM_ERROR.getResponseCode(), exception.getMessage());
    }
}
