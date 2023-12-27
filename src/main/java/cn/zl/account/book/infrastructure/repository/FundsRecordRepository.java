package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.architecture.JpaConfig;
import cn.zl.account.book.info.FundsRecordSearchInfo;
import cn.zl.account.book.infrastructure.DO.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTrendBo;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordRepository extends JpaRepository<FundsRecordEntity, Long>,
        JpaSpecificationExecutor<FundsRecordEntity> {

    /**
     * pagination
     *
     * @param recordSearchInfo conditions
     * @param pageable         page condition
     * @return page list
     */
    default Page<FundsRecordEntity> paginationRecord(FundsRecordSearchInfo recordSearchInfo,
                                                     Pageable pageable) {


        Specification<FundsRecordEntity> sp = (root, query, cb) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("invalid"), 0));

            List<Long> classifyIds = recordSearchInfo.getClassifyIds();
            if (CollectionUtils.isNotEmpty(classifyIds)) {
                predicates.add(cb.and(root.get("fundsRecordClassifyId").in(classifyIds)));
            }
            List<Long> accountIds = recordSearchInfo.getAccountIds();
            if (CollectionUtils.isNotEmpty(accountIds)) {
                predicates.add(cb.and(root.get("fundsAccountId").in(accountIds)));
            }

            LocalDateTime startTime = recordSearchInfo.getStartTime();
            if (Objects.nonNull(startTime)) {
                predicates.add(cb.greaterThan(root.get("fundsRecordTime"), startTime));
            }

            LocalDateTime endTime = recordSearchInfo.getEndTime();
            if (Objects.nonNull(endTime)) {
                predicates.add(cb.lessThan(root.get("fundsRecordTime"), endTime));
            }

            String recordKeyword = recordSearchInfo.getRecordKeyword();
            if (Objects.nonNull(recordKeyword)) {
                predicates.add(cb.like(root.get("fundsRecordDescribe"), recordKeyword));
            }

            Predicate[] p = new Predicate[predicates.size()];
            p = predicates.toArray(p);


            return query.where(p).getRestriction();
        };

        return findAll(sp, pageable);
    }

    /**
     * sum income or expenditure
     *
     * @param classifyIds   classify ids
     * @param startTime     startTime
     * @param endTime       endTime
     * @param entityManager entity manager
     * @return value
     */
    default Long sumOverview(List<Long> classifyIds, LocalDateTime startTime, LocalDateTime endTime, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<FundsRecordEntity> root = query.from(FundsRecordEntity.class);
        ArrayList<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("invalid"), 0));
        predicates.add(cb.and(root.get("fundsRecordClassifyId").in(classifyIds)));

        if (Objects.nonNull(startTime)) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fundsRecordTime"), startTime));
        }

        if (Objects.nonNull(endTime)) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fundsRecordTime"), endTime));
        }

        Predicate[] p = new Predicate[predicates.size()];
        p = predicates.toArray(p);
        query.multiselect(cb.coalesce(cb.sum(root.get("fundsRecordBalance")), 0));
        query.where(p);

        return entityManager.createQuery(query).getSingleResult();
    }

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
    List<AnalyzeTrendBo> queryFundsTrend(@Param("typeFormat") String typeFormat,
                                         @Param("startTime") LocalDate startTime,
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
    List<AnalyzeTopsBo> queryFundsTops(@Param("classifyType") Integer classifyType,
                                       @Param("startTime") LocalDate startTime,
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
    List<AnalyzeComposeBo> queryFundsCompose(@Param("classifyType") Integer classifyType,
                                             @Param("startTime") LocalDate startTime,
                                             @Param("endTime") LocalDate endTime);

}
