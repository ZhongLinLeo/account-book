package cn.zl.account.book.application.biz.funds;

import cn.zl.account.book.application.domain.FundsRecordClassifyDomainService;
import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.application.FundsRecordClassifyAppService;
import cn.zl.account.book.domain.converter.FundsRecordClassifyEntityConverter;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Service
public class FundsRecordClassifyAppServiceImpl implements FundsRecordClassifyAppService {

    @Resource
    private FundsRecordClassifyRepository fundsRecordClassifyRepository;

    @Resource
    private FundsRecordClassifyDomainService fundsRecordClassifyDomainService;

    @Override
    public void addClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        fundsRecordClassifyDomainService.addClassify(fundsRecordClassifyInfo);
    }

    @Override
    public void modifyClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        fundsRecordClassifyDomainService.modifyClassify(fundsRecordClassifyInfo);
    }

    @Override
    public void delClassify(Long classifyId) {
        fundsRecordClassifyDomainService.delClassify(classifyId);
    }

    @Override
    public List<FundsRecordClassifyInfo> listClassify() {
        List<FundsRecordClassifyEntity> classifyEntities = fundsRecordClassifyRepository.findAll();

        Map<Long, String> classifyMap = classifyEntities.stream()
                .filter(entity -> Objects.isNull(entity.getParentClassifyId()))
                .collect(Collectors
                        .toMap(FundsRecordClassifyEntity::getClassifyId, FundsRecordClassifyEntity::getClassifyName));

        return classifyEntities.stream().map(entity -> {
            FundsRecordClassifyInfo info = FundsRecordClassifyEntityConverter.entity2Info(entity);
            String parentClassifyName = classifyMap.get(entity.getParentClassifyId());
            info.setParentClassifyName(parentClassifyName);
            return info;
        }).collect(Collectors.toList());
    }
}
