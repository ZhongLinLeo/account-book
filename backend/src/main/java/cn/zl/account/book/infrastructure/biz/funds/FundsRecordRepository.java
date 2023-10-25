package cn.zl.account.book.infrastructure.biz.funds;

import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordRepository extends JpaRepository<FundsRecordEntity, Long> {
}
