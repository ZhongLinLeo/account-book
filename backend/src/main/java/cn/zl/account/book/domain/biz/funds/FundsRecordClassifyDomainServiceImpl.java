package cn.zl.account.book.domain.biz.funds;

import cn.zl.account.book.application.domain.FundsRecordClassifyDomainService;
import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.domain.converter.FundsRecordClassifyEntityConverter;
import cn.zl.account.book.domain.utils.SnowIdUtil;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lin.zl
 */
@Service
public class FundsRecordClassifyDomainServiceImpl implements FundsRecordClassifyDomainService {

    @Resource
    private FundsRecordClassifyRepository fundsRecordClassifyRepository;

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Override
    public void addClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        FundsRecordClassifyEntity entity = FundsRecordClassifyEntityConverter.info2entity(fundsRecordClassifyInfo);

        long classifyId = SnowIdUtil.nextId();
        entity.setClassifyId(classifyId);

        fundsRecordClassifyRepository.save(entity);
    }

    @Override
    public void modifyClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        FundsRecordClassifyEntity entity = FundsRecordClassifyEntityConverter.info2entity(fundsRecordClassifyInfo);

        fundsRecordClassifyRepository.save(entity);
    }

    @Override
    public void delClassify(Long classifyId) {
        // check the classify id used,todo
        fundsRecordClassifyRepository.deleteById(classifyId);
    }
}
