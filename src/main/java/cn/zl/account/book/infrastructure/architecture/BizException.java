package cn.zl.account.book.infrastructure.architecture;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author create by leo.zl on 2023/10/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    private final Integer messageCode;

    private final String message;

    public BizException(Integer messageCode, String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public BizException(ResponseStatusEnum responseStatusEnum) {
        this.messageCode = responseStatusEnum.getResponseCode();
        this.message = responseStatusEnum.getResponseMessage();
    }
}
