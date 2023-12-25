package cn.zl.account.book.application.impl;

import cn.zl.account.book.domain.FundsRecordClassifyDomainService;
import cn.zl.account.book.info.FundsRecordClassifyInfo;
import cn.zl.account.book.application.FundsRecordClassifyAppService;
import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.view.request.FundsClassifyQueryRequest;
import cn.zl.account.book.domain.converter.FundsRecordClassifyEntityConverter;
import cn.zl.account.book.architecture.BizAssert;
import cn.zl.account.book.architecture.BizException;
import cn.zl.account.book.infrastructure.repository.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.repository.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
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

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Override
    public void addClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        final FundsRecordClassifyEntity conditions = new FundsRecordClassifyEntity();
        conditions.setClassifyName(fundsRecordClassifyInfo.getClassifyName());
        final boolean exists = fundsRecordClassifyRepository.exists(Example.of(conditions));
        BizAssert.isTrue(exists, ResponseStatusEnum.CLASSIFY_EXIST);

        fundsRecordClassifyDomainService.addClassify(fundsRecordClassifyInfo);
    }

    @Override
    public void modifyClassify(FundsRecordClassifyInfo fundsRecordClassifyInfo) {
        Optional<FundsRecordClassifyEntity> optional = fundsRecordClassifyRepository.findById(fundsRecordClassifyInfo.getClassifyId());
        FundsRecordClassifyEntity classifyEntity = optional
                .orElseThrow(() -> new BizException(ResponseStatusEnum.CLASSIFY_NONE_EXIST));

        fundsRecordClassifyDomainService.modifyClassify(fundsRecordClassifyInfo, classifyEntity);
    }

    @Override
    public void delClassify(Long classifyId) {
        final FundsRecordEntity conditions = new FundsRecordEntity();
        conditions.setFundsRecordClassifyId(classifyId);
        boolean exists = fundsRecordRepository.exists(Example.of(conditions));

        BizAssert.isTrue(exists, ResponseStatusEnum.CLASSIFY_USING);

        fundsRecordClassifyDomainService.delClassify(classifyId);
    }

    @Override
    public List<FundsRecordClassifyInfo> listClassify() {
        final FundsRecordClassifyEntity conditions = new FundsRecordClassifyEntity();
        List<FundsRecordClassifyEntity> classifyEntities = fundsRecordClassifyRepository.findAll(Example.of(conditions));
        return classifyEntities.stream().map(FundsRecordClassifyEntityConverter::entity2Info).collect(Collectors.toList());
    }

    @Override
    public Page<FundsRecordClassifyInfo> paginationClassify(FundsClassifyQueryRequest pageQuery) {
        PageRequest pageRequest = PageRequest.of(pageQuery.getCurrent(), pageQuery.getPageSize(),
                Sort.by(Sort.Direction.fromString(pageQuery.getOrder()), pageQuery.getSortFiled()));

        Page<FundsRecordClassifyEntity> fundsClassifies = fundsRecordClassifyRepository
                .paginationClassify(pageQuery.getClassifyName(), pageRequest);

        List<FundsRecordClassifyInfo> fundsClassifyInfos = fundsClassifies.get()
                .map(FundsRecordClassifyEntityConverter::entity2Info)
                .collect(Collectors.toList());

        return new PageImpl<>(fundsClassifyInfos, pageRequest, fundsClassifies.getTotalElements());
    }

    @Override
    public FundsRecordClassifyInfo findClassify(Long classifyId) {
        final Optional<FundsRecordClassifyEntity> originOptional = fundsRecordClassifyRepository.findById(classifyId);

        final FundsRecordClassifyEntity classifyEntity =
                originOptional.orElseThrow(() -> new BizException(ResponseStatusEnum.FUNDS_RECORD_NONE_EXIST));

        return FundsRecordClassifyEntityConverter.entity2Info(classifyEntity);
    }
}
