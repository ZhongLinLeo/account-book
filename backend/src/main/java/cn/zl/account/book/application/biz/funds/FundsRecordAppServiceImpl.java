package cn.zl.account.book.application.biz.funds;

import cn.zl.account.book.application.domain.FundsRecordDomainService;
import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.FundsRecordAppService;
import cn.zl.account.book.controller.request.PaginationFundsRecordRequestDTO;
import cn.zl.account.book.domain.converter.FundsRecordEntityConverter;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Service
public class FundsRecordAppServiceImpl implements FundsRecordAppService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Resource
    private FundsRecordDomainService fundsRecordDomainService;

    @Override
    public void recordFunds(FundsRecordInfo fundsRecordInfo) {
        fundsRecordDomainService.recordFunds(fundsRecordInfo);
    }

    @Override
    public void modifyFundsRecord(FundsRecordInfo fundsRecordInfo) {
        fundsRecordDomainService.modifyFundRecord(fundsRecordInfo);
    }

    @Override
    public void delFundsRecord(Long recordId) {
        fundsRecordDomainService.delFundRecord(recordId);
    }

    @Override
    public Page<FundsRecordInfo> paginationFundsRecord(PaginationFundsRecordRequestDTO paginationReq) {
        PageRequest pageRequest = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize());

        Page<FundsRecordEntity> fundsRecords = fundsRecordRepository.findAll(pageRequest);

        List<FundsRecordInfo> content = fundsRecords.get()
                .map(FundsRecordEntityConverter::entity2Info)
                .collect(Collectors.toList());

        return new PageImpl<>(content,pageRequest,fundsRecords.getTotalElements());
    }

    @Override
    public void importFundsRecord(MultipartFile excelFile) {
        fundsRecordDomainService.importFundsRecord(excelFile);
    }
}
