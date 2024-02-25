package cn.zl.account.book.domain.impl;

import cn.zl.account.book.domain.FundsRecordDomainService;
import cn.zl.account.book.domain.converter.FundsRecordEntityConverter;
import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import cn.zl.account.book.infrastructure.repository.FundsRecordRepository;
import cn.zl.account.book.util.BeanCopyUtils;
import cn.zl.account.book.util.SnowIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Service
@Slf4j
public class FundsRecordDomainServiceImpl implements FundsRecordDomainService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Override
    public void recordFunds(FundsRecordInfo fundsRecordInfo) {
        FundsRecordEntity entity = FundsRecordEntityConverter.info2Entity(fundsRecordInfo);

        // generate account id
        final long fundsRecordId = SnowIdUtils.nextId();
        entity.setFundsRecordId(fundsRecordId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setModifyTime(LocalDateTime.now());

        fundsRecordRepository.save(entity);
    }

    @Override
    public void modifyFundRecord(FundsRecordEntity classifyEntity, FundsRecordInfo fundsRecordInfo) {
        BeanUtils.copyProperties(fundsRecordInfo, classifyEntity, BeanCopyUtils.getNullPropertyNames(fundsRecordInfo));
        classifyEntity.setModifyTime(LocalDateTime.now());

        fundsRecordRepository.save(classifyEntity);
    }

    @Override
    public void delFundRecord(FundsRecordEntity recordEntity) {
        recordEntity.setInvalid(1);
        fundsRecordRepository.save(recordEntity);
    }
}
