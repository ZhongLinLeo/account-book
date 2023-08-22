package cn.zl.account.book.infrastructure.biz.funds;

import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordRepository extends PagingAndSortingRepository<FundsRecordEntity, Long> {
}
