package cn.zl.account.book.infrastructure.biz.funds;

import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordClassifyRepository extends JpaRepository<FundsRecordClassifyEntity, Long> {
}
