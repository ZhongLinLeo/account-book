package cn.zl.account.book.infrastructure.biz.funds;

import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author lin.zl
 */
@Repository
public interface FundsRecordClassifyRepository extends JpaRepository<FundsRecordClassifyEntity, Long> {

    /**
     * delete by logical
     *
     * @param classifyId classify id
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update FundsRecordClassifyEntity set invalid = 1 where classifyId = :classifyId")
    void deleteLogical(@Param("classifyId") Long classifyId);

    /**
     * pagination
     *
     * @param classifyName classify name
     * @param pageable     page condition
     * @return page list
     */
    @Query(value = "SELECT * FROM funds_record_classify WHERE invalid = 0 AND (classify_name like :classifyName or :classifyName is null)",
            countQuery = "SELECT COUNT(*) FROM funds_record_classify WHERE invalid = 0 AND (classify_name like %:classifyName% or :classifyName is null)",
            nativeQuery = true)
    Page<FundsRecordClassifyEntity> paginationClassify(@Param("classifyName") String classifyName, Pageable pageable);

}
