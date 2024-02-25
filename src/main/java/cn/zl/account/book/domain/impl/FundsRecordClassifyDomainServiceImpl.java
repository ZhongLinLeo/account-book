package cn.zl.account.book.domain.impl;

import cn.zl.account.book.domain.FundsRecordClassifyDomainService;
import cn.zl.account.book.info.FundsRecordClassifyInfo;
import cn.zl.account.book.domain.converter.FundsRecordClassifyEntityConverter;
import cn.zl.account.book.infrastructure.repository.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.util.SnowIdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Service
public class FundsRecordClassifyDomainServiceImpl implements FundsRecordClassifyDomainService {

    @Resource
    private FundsRecordClassifyRepository fundsRecordClassifyRepository;

    @Override
    public void addClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        FundsRecordClassifyEntity entity = FundsRecordClassifyEntityConverter.info2entity(fundsRecordClassifyInfo);

        entity.setCreateTime(LocalDateTime.now());
        entity.setModifyTime(LocalDateTime.now());

        long classifyId = SnowIdUtils.nextId();
        entity.setClassifyId(classifyId);

        fundsRecordClassifyRepository.save(entity);
    }

    @Override
    public void modifyClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo, FundsRecordClassifyEntity classifyEntity) {
        classifyEntity.setModifyTime(LocalDateTime.now());
        classifyEntity.setClassifyType(fundsRecordClassifyInfo.getClassifyType());
        classifyEntity.setClassifyName(fundsRecordClassifyInfo.getClassifyName());
        classifyEntity.setClassifyDescribe(fundsRecordClassifyInfo.getClassifyDescribe());
        fundsRecordClassifyRepository.save(classifyEntity);
    }

    @Override
    public void delClassify(FundsRecordClassifyEntity classifyEntity) {
        classifyEntity.setInvalid(1);
        fundsRecordClassifyRepository.save(classifyEntity);
    }
}
