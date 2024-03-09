package cn.zl.account.book.application.strategy;

import cn.zl.account.book.enums.FundsRecordImportTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lin.zl
 */
@Slf4j
@Component
public class AliRecordImportStrategy extends BaseFundsRecordImportStrategy {



    @Override
    public FundsRecordImportTypeEnum strategyName() {
        return FundsRecordImportTypeEnum.ALI_PAY;
    }
}
