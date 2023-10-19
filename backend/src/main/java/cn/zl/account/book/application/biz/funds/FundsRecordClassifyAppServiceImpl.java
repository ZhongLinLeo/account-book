package cn.zl.account.book.application.biz.funds;

import cn.zl.account.book.application.domain.FundsRecordClassifyDomainService;
import cn.zl.account.book.application.info.FundsRecordClassifyInfo;
import cn.zl.account.book.controller.application.FundsRecordClassifyAppService;
import cn.zl.account.book.controller.request.FundsClassifyQueryRequest;
import cn.zl.account.book.domain.converter.FundsRecordClassifyEntityConverter;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
        return classifyEntities.stream().map(FundsRecordClassifyEntityConverter::entity2Info).collect(Collectors.toList());
    }

    @Override
    public Page<FundsRecordClassifyInfo> paginationClassify(FundsClassifyQueryRequest pageQuery) {
        PageRequest pageRequest = PageRequest.of(pageQuery.getPageNumber(), pageQuery.getPageSize());

        Page<FundsRecordClassifyEntity> fundsClassifies = fundsRecordClassifyRepository.findAll(pageRequest);

        List<FundsRecordClassifyInfo> fundsClassifyInfos = fundsClassifies.get()
                .map(FundsRecordClassifyEntityConverter::entity2Info)
                .collect(Collectors.toList());

        return new PageImpl<>(fundsClassifyInfos, pageRequest, fundsClassifies.getTotalElements());
    }
}
