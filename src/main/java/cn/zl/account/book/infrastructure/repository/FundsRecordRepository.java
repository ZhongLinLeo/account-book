package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.info.FundsRecordSearchInfo;
import cn.zl.account.book.infrastructure.DO.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTrendBo;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

}
