package cn.zl.account.book.controller.application;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.request.FundsRecordQueryRequest;
import org.springframework.data.domain.Page;
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
     * query record
     *
     * @param paginationReq query funds record conditions
     * @return page info
     */
    Page<FundsRecordInfo> paginationFundsRecord(FundsRecordQueryRequest paginationReq);

    /**
     * import funds records
     *
     * @param excelFile excel file
     */
    void importFundsRecord(MultipartFile excelFile);
}
