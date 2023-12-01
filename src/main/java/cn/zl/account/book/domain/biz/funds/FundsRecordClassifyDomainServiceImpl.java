package cn.zl.account.book.domain.biz.funds;

import cn.zl.account.book.application.domain.FundsRecordClassifyDomainService;
import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.domain.converter.FundsRecordClassifyEntityConverter;
import cn.zl.account.book.domain.utils.SnowIdUtil;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
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

        long classifyId = SnowIdUtil.nextId();
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
    public void delClassify(Long classifyId) {
        fundsRecordClassifyRepository.deleteLogical(classifyId);
    }
}
