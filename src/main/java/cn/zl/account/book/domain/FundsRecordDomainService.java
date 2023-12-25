package cn.zl.account.book.domain;

import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lin.zl
 */
public interface FundsRecordDomainService {
    /**
     * record
     *
     * @param fundsRecordInfo funds record info
     */
    void recordFunds(FundsRecordInfo fundsRecordInfo);

    /**
     * modify record
     *
     * @param classifyEntity classify info
     * @param fundsRecordInfo funds record info
     */
    void modifyFundRecord(FundsRecordEntity classifyEntity, FundsRecordInfo fundsRecordInfo);

    /**
     * del record
     *
     * @param recordId funds record id
     */
    void delFundRecord(Long recordId);

    /**
     * import funds records
     *
     * @param excelFile excel file
     */
    void importFundsRecord(MultipartFile excelFile);
}
