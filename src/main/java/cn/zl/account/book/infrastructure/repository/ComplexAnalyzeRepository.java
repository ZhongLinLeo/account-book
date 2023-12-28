package cn.zl.account.book.infrastructure.repository;

import cn.zl.account.book.infrastructure.DO.AnalyzeComposeBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTopsBo;
import cn.zl.account.book.infrastructure.DO.AnalyzeTrendBo;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分析相关复杂查询
 *
 * @author lin.zl
 */
@Repository
public class ComplexAnalyzeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String START_TIME_FIELD = "startTime";
    private static final String END_TIME_FIELD = "endTime";


    /**
     * sum income or expenditure
     *
     * @param classifyIds classify ids
     * @param startTime   startTime
     * @param endTime     endTime
     * @return value
     */
    public Long sumOverview(List<Long> classifyIds, LocalDateTime startTime, LocalDateTime endTime) {
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

    public List<AnalyzeTrendBo> queryFundsTrend(String format, LocalDate startTime, LocalDate endTime) {
        String trendSql = "SELECT FUNCTION('date_format',record.fundsRecordTime, :typeFormat) AS fundsRecordDate , " +
                "       SUM(record.fundsRecordBalance) AS totalFundsBalance, " +
                "       classify.classifyType AS classifyType " +
                "FROM FundsRecordEntity record " +
                "         JOIN FundsRecordClassifyEntity classify ON record.fundsRecordClassifyId = classify.classifyId " +
                "WHERE record.fundsRecordTime BETWEEN :startTime AND :endTime " +
                "  AND classify.includeAnalyze = 1 " +
                "  AND record.invalid = 0 " +
                "GROUP BY classifyType, fundsRecordDate";

        TypedQuery<Tuple> query = entityManager.createQuery(trendSql, Tuple.class);

        List<Tuple> resultList = query
                .setParameter("typeFormat", format)
                .setParameter(START_TIME_FIELD, LocalDateTime.of(startTime, LocalTime.MIN))
                .setParameter(END_TIME_FIELD, LocalDateTime.of(endTime, LocalTime.MAX))
                .getResultList();
        return resultList.stream().map(tuple -> {
            AnalyzeTrendBo trendBo = new AnalyzeTrendBo();
            trendBo.setFundsRecordDate(tuple.get("fundsRecordDate", String.class));
            trendBo.setTotalFundsBalance(tuple.get("totalFundsBalance", Long.class));
            trendBo.setClassifyType(tuple.get("classifyType", Integer.class));
            return trendBo;
        }).collect(Collectors.toList());
    }

    public List<AnalyzeTopsBo> queryFundsTops(Integer classifyType, LocalDate startTime, LocalDate endTime) {
        String topSql = "SELECT record.fundsRecordBalance AS fundsRecordBalance, " +
                "       classify.classifyName AS classifyName, " +
                "       record.fundsRecordTime AS fundsRecordTime, " +
                "       record.fundsRecordDescribe AS fundsRecordDesc " +
                "FROM FundsRecordEntity record " +
                "         JOIN FundsRecordClassifyEntity classify ON record.fundsRecordClassifyId = classify.classifyId " +
                "WHERE classify.classifyType = :classifyType " +
                "  AND record.fundsRecordTime BETWEEN :startTime AND :endTime " +
                "  AND classify.includeAnalyze = 1 " +
                "  AND record.invalid = 0 " +
                "ORDER BY record.fundsRecordBalance DESC";

        TypedQuery<Tuple> query = entityManager.createQuery(topSql, Tuple.class);

        List<Tuple> resultList = query
                .setParameter("classifyType", classifyType)
                .setParameter(START_TIME_FIELD, LocalDateTime.of(startTime, LocalTime.MIN))
                .setParameter(END_TIME_FIELD, LocalDateTime.of(endTime, LocalTime.MAX))
                .setMaxResults(10)
                .getResultList();

        return resultList.stream().map(tuple -> {
            AnalyzeTopsBo topsBo = new AnalyzeTopsBo();
            topsBo.setFundsRecordBalance(tuple.get("fundsRecordBalance", Long.class));
            topsBo.setFundsRecordDesc(tuple.get("fundsRecordDesc", String.class));
            topsBo.setFundsRecordTime(tuple.get("fundsRecordTime", LocalDateTime.class));
            topsBo.setClassifyName(tuple.get("classifyName", String.class));

            return topsBo;
        }).collect(Collectors.toList());
    }

    public List<AnalyzeComposeBo> queryFundsCompose(Integer classifyType, LocalDate startTime, LocalDate endTime) {
        String topSql = "SELECT classify.classifyName AS classifyName, " +
                "        classify.classifyId AS classifyId, " +
                "       SUM(record.fundsRecordBalance) AS totalFundsBalance " +
                "FROM FundsRecordEntity record " +
                "         JOIN FundsRecordClassifyEntity classify ON record.fundsRecordClassifyId = classify.classifyId " +
                "WHERE classify.classifyType = :classifyType " +
                "  AND classify.includeAnalyze = 1 " +
                "  AND record.invalid = 0 " +
                "  AND record.fundsRecordTime BETWEEN :startTime AND :endTime " +
                "GROUP BY classifyName, classifyId";

        TypedQuery<Tuple> query = entityManager.createQuery(topSql, Tuple.class);

        List<Tuple> resultList = query
                .setParameter("classifyType", classifyType)
                .setParameter(START_TIME_FIELD, LocalDateTime.of(startTime, LocalTime.MIN))
                .setParameter(END_TIME_FIELD, LocalDateTime.of(endTime, LocalTime.MAX))
                .getResultList();

        return resultList.stream().map(tuple -> {
            AnalyzeComposeBo composeBo = new AnalyzeComposeBo();
            composeBo.setClassifyName(tuple.get("classifyName", String.class));
            composeBo.setClassifyId(tuple.get("classifyId", Long.class));
            composeBo.setTotalFundsBalance(tuple.get("totalFundsBalance", Long.class));
            return composeBo;
        }).collect(Collectors.toList());
    }

}
