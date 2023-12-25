package cn.zl.account.book.application;

import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.info.FundsRecordSearchInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lin.zl
 */
public interface FundsRecordAppService {

    /**
     * record
     *
     * @param fundsRecordInfo funds record info
     */
    void recordFunds(FundsRecordInfo fundsRecordInfo);

    /**
     * modify record
     *
     * @param fundsRecordInfo funds record info
     */
    void modifyFundsRecord(FundsRecordInfo fundsRecordInfo);

    /**
     * del record
     *
     * @param recordId funds record id
     */
    void delFundsRecord(Long recordId);

    /**
     * import funds records
     *
     * @param excelFile excel file
     */
    void importFundsRecord(MultipartFile excelFile);

    /**
     * search records
     *
     * @param pageRequest page request
     * @param recordSearchInfo search conditions
     * @return records
     */
    Page<FundsRecordInfo> paginationFundsRecord(PageRequest pageRequest, FundsRecordSearchInfo recordSearchInfo);
}
