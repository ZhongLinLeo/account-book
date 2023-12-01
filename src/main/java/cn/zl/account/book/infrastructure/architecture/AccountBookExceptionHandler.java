package cn.zl.account.book.infrastructure.architecture;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.response.NormalResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author create by leo.zl on 2023/10/22
 */
@RestControllerAdvice
public class AccountBookExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public NormalResponse<Boolean> bizExceptionHandler(BizException bizException) {
        return NormalResponse.wrapBizExceptionResponse(bizException, Boolean.FALSE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public NormalResponse<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));

        String messagePattern = ResponseStatusEnum.REQUEST_PARAM_ERROR.getResponseMessage();
        String message = messagePattern.replace("{0}", errors);

        BizException bizException = new BizException(ResponseStatusEnum.REQUEST_PARAM_ERROR.getResponseCode(),
                message);

        return NormalResponse.wrapBizExceptionResponse(bizException, errors);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public NormalResponse<String> exceptionHandler(RuntimeException exception) {
        return NormalResponse.wrapResponse(ResponseStatusEnum.SYSTEM_ERROR.getResponseCode(), exception.getMessage());
    }
}
