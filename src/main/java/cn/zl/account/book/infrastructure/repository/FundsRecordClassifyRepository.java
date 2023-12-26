package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordClassifyRepository extends JpaRepository<FundsRecordClassifyEntity, Long>,
        JpaSpecificationExecutor<FundsRecordClassifyEntity> {

    /**
     * pagination
     *
     * @param classifyName classify name
     * @param pageable     page condition
     * @return page list
     */
    default Page<FundsRecordClassifyEntity> paginationClassify(String classifyName, Pageable pageable) {
        Specification<FundsRecordClassifyEntity> sp = (root, query, cb) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("invalid"), 0));

            if (StringUtils.isNoneBlank(classifyName)) {
                predicates.add(cb.like(root.get("classifyName"), classifyName));
            }

            Predicate[] p = new Predicate[predicates.size()];
            p = predicates.toArray(p);
            return query.where(p).getRestriction();
        };
        return findAll(sp, pageable);
    }

}
