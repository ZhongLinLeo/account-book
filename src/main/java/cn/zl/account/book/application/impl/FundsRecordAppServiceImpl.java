package cn.zl.account.book.application.impl;

import cn.zl.account.book.application.FundsRecordAppService;
import cn.zl.account.book.architecture.BizException;
import cn.zl.account.book.domain.AccountDomainService;
import cn.zl.account.book.domain.FundsRecordDomainService;
import cn.zl.account.book.domain.converter.FundsRecordEntityConverter;
import cn.zl.account.book.enums.ClassifyTypeEnum;
import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.info.FundsRecordSearchInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import cn.zl.account.book.infrastructure.repository.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.repository.FundsRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Service
public class FundsRecordAppServiceImpl implements FundsRecordAppService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Resource
    private FundsRecordClassifyRepository fundsRecordClassifyRepository;

    @Resource
    private FundsRecordDomainService fundsRecordDomainService;

    @Resource
    private AccountDomainService accountDomainService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void recordFunds(FundsRecordInfo fundsRecordInfo) {
        final Long fundsRecordClassifyId = fundsRecordInfo.getFundsRecordClassifyId();
        final FundsRecordClassifyEntity classifyEntity = fundsRecordClassifyRepository
                .findById(fundsRecordClassifyId)
                .orElseThrow(() -> new BizException(ResponseStatusEnum.CLASSIFY_NONE_EXIST));

        // 从账户中增减
        accountDomainService.transaction(fundsRecordInfo.getFundsAccountId(), fundsRecordInfo.getFundsRecordBalance()
                , ClassifyTypeEnum.findClassifyTypeEnum(classifyEntity.getClassifyType()));

        // 记录
        fundsRecordDomainService.recordFunds(fundsRecordInfo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void modifyFundsRecord(FundsRecordInfo fundsRecordInfo) {
        Optional<FundsRecordEntity> optional = fundsRecordRepository.findById(fundsRecordInfo.getFundsRecordId());
        FundsRecordEntity classifyEntity = optional
                .orElseThrow(() -> new BizException(ResponseStatusEnum.FUNDS_RECORD_NONE_EXIST));

        // 修改流水信息时，先在原始记录的账户中，修改相应的金额，再对当前账户进行修改
        accountDomainService.transaction(classifyEntity.getFundsAccountId(), -classifyEntity.getFundsRecordBalance());

        accountDomainService.transaction(fundsRecordInfo.getFundsAccountId(), fundsRecordInfo.getFundsRecordBalance());

        fundsRecordDomainService.modifyFundRecord(classifyEntity, fundsRecordInfo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delFundsRecord(Long recordId) {
        Optional<FundsRecordEntity> optional = fundsRecordRepository.findById(recordId);
        FundsRecordEntity classifyEntity = optional
                .orElseThrow(() -> new BizException(ResponseStatusEnum.FUNDS_RECORD_NONE_EXIST));

        accountDomainService.transaction(classifyEntity.getFundsAccountId(), -classifyEntity.getFundsRecordBalance());

        fundsRecordDomainService.delFundRecord(recordId);
    }

    @Override
    public void importFundsRecord(MultipartFile excelFile) {
        fundsRecordDomainService.importFundsRecord(excelFile);
    }

    @Override
    public Page<FundsRecordInfo> paginationFundsRecord(PageRequest pageRequest, FundsRecordSearchInfo recordSearchInfo) {
        Page<FundsRecordEntity> fundsRecords = fundsRecordRepository.paginationRecord(recordSearchInfo, pageRequest);

        List<FundsRecordInfo> content = fundsRecords.get()
                .map(FundsRecordEntityConverter::entity2Info)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, fundsRecords.getTotalElements());
    }
}
