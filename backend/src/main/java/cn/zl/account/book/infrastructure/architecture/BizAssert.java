package cn.zl.account.book.infrastructure.architecture;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;

import java.util.Objects;

/**
 * @author create by leo.zl on 2023/10/22
 */
public class BizAssert {

    public static void isTrue(Boolean target, ResponseStatusEnum responseStatusEnum) {
        if (Objects.equals(target, Boolean.TRUE)) {
            throw new BizException(responseStatusEnum);
        }
    }

    public static void isFalse(Boolean target, ResponseStatusEnum responseStatusEnum) {
        if (Objects.equals(target, Boolean.FALSE)) {
            throw new BizException(responseStatusEnum);
        }
    }

    private BizAssert() {
    }
}
