package cn.zl.account.book.infrastructure.biz.funds;

import cn.zl.account.book.controller.request.FundsRecordQueryRequest;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordRepository extends JpaRepository<FundsRecordEntity, Long> {

    /**
     * pagination
     *
     * @param paginationReq conditions
     * @param pageable      page condition
     * @return page list
     */
    @Query(value = "SELECT * FROM funds_record WHERE invalid = 0 " +
            "AND (funds_record_describe like :#{paginationReq.recordKeyword} or :#{paginationReq.recordKeyword} is null) " +
            "AND (funds_record_time > :#{paginationReq.startTime} or :#{paginationReq.startTime} is null) " +
            "AND (funds_record_time < :#{paginationReq.endTime} or :#{paginationReq.endTime} is null) " +
            "AND (funds_record_classify_id = :#{paginationReq.classifyId} or :#{paginationReq.classifyId} is null) " +
            "AND (funds_account_id = :#{paginationReq.accountId} or :#{paginationReq.accountId} is null)",
            countQuery = "SELECT COUNT(*) FROM funds_record WHERE invalid = 0 " +
                    "AND (funds_record_describe like :#{paginationReq.recordKeyword} or :#{paginationReq.recordKeyword} is null) " +
                    "AND (funds_record_time > :#{paginationReq.startTime} or :#{paginationReq.startTime} is null) " +
                    "AND (funds_record_time < :#{paginationReq.endTime} or :#{paginationReq.endTime} is null) " +
                    "AND (funds_record_classify_id = :#{paginationReq.classifyId} or :#{paginationReq.classifyId} is null) " +
                    "AND (funds_account_id = :#{paginationReq.accountId} or :#{paginationReq.accountId} is null)",
            nativeQuery = true)
    Page<FundsRecordEntity> paginationRecord(@Param("paginationReq") FundsRecordQueryRequest paginationReq, Pageable pageable);
}
