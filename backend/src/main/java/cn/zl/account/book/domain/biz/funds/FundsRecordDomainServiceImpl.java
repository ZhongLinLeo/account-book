package cn.zl.account.book.domain.biz.funds;

import cn.zl.account.book.application.domain.FundsRecordDomainService;
import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.domain.converter.FundsRecordEntityConverter;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import cn.zl.account.book.infrastructure.utils.SnowIdUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lin.zl
 */
@Service
public class FundsRecordDomainServiceImpl implements FundsRecordDomainService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Override
    public void recordFunds(FundsRecordInfo fundsRecordInfo) {
        FundsRecordEntity entity = FundsRecordEntityConverter.info2Entity(fundsRecordInfo);

        // generate account id
        final long fundsRecordId = SnowIdUtil.nextId();
        entity.setFundsRecordId(fundsRecordId);

        fundsRecordRepository.save(entity);
    }

    @Override
    public void modifyFundRecord(FundsRecordInfo fundsRecordInfo) {
        FundsRecordEntity entity = FundsRecordEntityConverter.info2Entity(fundsRecordInfo);
        fundsRecordRepository.save(entity);
    }

    @Override
    public void delFundRecord(Long recordId) {
        fundsRecordRepository.deleteById(recordId);
    }
}
