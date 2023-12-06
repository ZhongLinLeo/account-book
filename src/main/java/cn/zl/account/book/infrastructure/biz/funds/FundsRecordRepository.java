package cn.zl.account.book.infrastructure.biz.funds;

import cn.zl.account.book.controller.request.FundsRecordQueryRequest;
import cn.zl.account.book.infrastructure.bo.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.bo.AnalyzeTrendBo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordRepository extends JpaRepository<FundsRecordEntity, Long> {
    /**
     * delete by logical
     *
     * @param fundsRecordId classify id
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update FundsRecordEntity set invalid = 1 where fundsRecordId = :fundsRecordId")
    void deleteLogical(@Param("fundsRecordId") Long fundsRecordId);

    /**
     * pagination
     *
     * @param paginationReq conditions
     * @param pageable      page condition
     * @return page list
     */
    @Query(value = "SELECT * FROM funds_record WHERE invalid = 0 " +
            "AND (funds_record_describe like :#{#paginationReq.recordKeyword} or :#{#paginationReq.recordKeyword} is null) " +
            "AND (funds_record_time > :#{#paginationReq.startTime} or :#{#paginationReq.startTime} is null) " +
            "AND (funds_record_time < :#{#paginationReq.endTime} or :#{#paginationReq.endTime} is null) " +
            "AND (funds_record_classify_id = :#{#paginationReq.classifyId} or :#{#paginationReq.classifyId} is null) " +
            "AND (funds_account_id = :#{#paginationReq.accountId} or :#{#paginationReq.accountId} is null)",
            countQuery = "SELECT COUNT(*) FROM funds_record WHERE invalid = 0 " +
                    "AND (funds_record_describe like :#{#paginationReq.recordKeyword} or :#{#paginationReq.recordKeyword} is null) " +
                    "AND (funds_record_time > :#{#paginationReq.startTime} or :#{#paginationReq.startTime} is null) " +
                    "AND (funds_record_time < :#{#paginationReq.endTime} or :#{#paginationReq.endTime} is null) " +
                    "AND (funds_record_classify_id = :#{#paginationReq.classifyId} or :#{#paginationReq.classifyId} is null) " +
                    "AND (funds_account_id = :#{#paginationReq.accountId} or :#{#paginationReq.accountId} is null)",
            nativeQuery = true)
    Page<FundsRecordEntity> paginationRecord(@Param("paginationReq") FundsRecordQueryRequest paginationReq, Pageable pageable);

    /**
     * sum income or expenditure
     *
     * @param classifyType    classify type
     * @param fundsRecordTime fundsRecordTime
     * @return value
     */
    @Query(value = "select sum(funds_record_balance)  from funds_record " +
            "where funds_record_classify_id in (select classify_id from funds_record_classify where classify_type = :classifyType " +
            "    and include_analyze = 1) " +
            "  and (funds_record_time > :fundsRecordTime or :fundsRecordTime is null)" +
            "  and  invalid = 0 ", nativeQuery = true)
    Long sumOverview(@Param("classifyType") Integer classifyType, @Param("fundsRecordTime") LocalDate fundsRecordTime);

    /**
     * query funds trend
     *
     * @param typeFormat trend type format
     * @param startTime  start time
     * @param endTime    end time
     * @return value
     */
    @Query(value = "select date_format(record.funds_record_time, :typeFormat) as fundsRecordDate, " +
            "       sum(record.funds_record_balance)                  totalFundsBalance, " +
            "       classify.classify_type classifyType " +
            "from funds_record record " +
            "         join funds_record_classify classify on record.funds_record_classify_id = classify.classify_id " +
            "where record.funds_record_time between :startTime and :endTime " +
            "  and classify.include_analyze = 1 " +
            "  and record.invalid = 0 " +
            "group by classifyType, fundsRecordDate", nativeQuery = true)
    List<AnalyzeTrendBo> queryFundsTrend(@Param("typeFormat") String typeFormat, @Param("startTime") LocalDate startTime,
                                         @Param("endTime") LocalDate endTime);

    /**
     * query funds tops
     *
     * @param classifyType trend type format
     * @param startTime    start time
     * @param endTime      end time
     * @return value
     */
    @Query(value = "select record.funds_record_balance fundsRecordBalance, " +
            "       classify.classify_name classifyName, " +
            "       record.funds_record_time fundsRecordTime, " +
            "       record.funds_record_describe fundsRecordDesc " +
            "from funds_record record " +
            "         join funds_record_classify classify on record.funds_record_classify_id = classify.classify_id " +
            "where classify.classify_type = :classifyType " +
            "  and record.funds_record_time between :startTime and :endTime " +
            "  and classify.include_analyze = 1 " +
            "  and record.invalid = 0 " +
            "order by record.funds_record_balance desc " +
            "limit 10", nativeQuery = true)
    List<AnalyzeTopsBo> queryFundsTops(@Param("classifyType") Integer classifyType, @Param("startTime") LocalDate startTime,
                                       @Param("endTime") LocalDate endTime);

    /**
     * query funds compose
     *
     * @param classifyType trend type format
     * @param startTime    start time
     * @param endTime      end time
     * @return value
     */
    @Query(value = "select classify.classify_name           classifyName, " +
            "       sum(record.funds_record_balance) totalFundsBalance " +
            "from funds_record record " +
            "         join funds_record_classify classify on record.funds_record_classify_id = classify.classify_id " +
            "where classify.classify_type = :classifyType " +
            "  and classify.include_analyze = 1 " +
            "  and record.invalid = 0 " +
            "  and record.funds_record_time between :startTime and :endTime " +
            "group by classifyName", nativeQuery = true)
    List<AnalyzeComposeBo> queryFundsCompose(@Param("classifyType") Integer classifyType, @Param("startTime") LocalDate startTime,
                                             @Param("endTime") LocalDate endTime);

}
