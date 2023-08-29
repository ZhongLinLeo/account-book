package cn.zl.account.book.infrastructure.biz.account;

import cn.zl.account.book.infrastructure.entity.AccountEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * account repository
 *
 * @author lin.zl
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long>, JpaSpecificationExecutor<AccountEntity> {

    /**
     * find all invalid
     *
     * @return
     */
    default List<AccountEntity> findAccountEntities() {
        Specification<AccountEntity> specification = (root, query, criteriaBuilder) -> query
                .where(criteriaBuilder.equal(root.get("invalid"), 0)).getRestriction();
        return findAll(specification);
    }
}
