package cn.zl.account.book.application.impl;

import cn.zl.account.book.application.factory.AnalyzeServiceFactory;
import cn.zl.account.book.application.strategy.BaseAnalyzeStrategy;
import cn.zl.account.book.application.FundsAnalyzeAppService;
import cn.zl.account.book.enums.AccountTypeEnum;
import cn.zl.account.book.enums.ClassifyTypeEnum;
import cn.zl.account.book.enums.TrendAnalyzeEnum;
import cn.zl.account.book.info.FundsComposeInfo;
import cn.zl.account.book.info.FundsOverviewInfo;
import cn.zl.account.book.info.FundsRecordTopInfo;
import cn.zl.account.book.info.FundsTrendInfo;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.infrastructure.repository.AccountRepository;
import cn.zl.account.book.infrastructure.repository.ComplexAnalyzeRepository;
import cn.zl.account.book.infrastructure.repository.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.repository.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author lin.zl
 */
@Service
@Slf4j
public class FundsAnalyzeAppServiceImpl implements FundsAnalyzeAppService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Resource
    private FundsRecordClassifyRepository fundsRecordClassifyRepository;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private ComplexAnalyzeRepository complexAnalyzeRepository;

    @Override
    public FundsOverviewInfo fundsOverview() {
        List<FundsRecordClassifyEntity> classifyEntities = fundsRecordClassifyRepository.listAnalyzeClassify();

        List<Long> incomeClassify = classifyEntities.stream()
                .filter(e -> Objects.equals(e.getClassifyType(), ClassifyTypeEnum.INCOME.getClassifyType()))
                .map(FundsRecordClassifyEntity::getClassifyId)
                .collect(Collectors.toList());

        List<Long> expenditureClassify = classifyEntities.stream()
                .filter(e -> Objects.equals(e.getClassifyType(), ClassifyTypeEnum.EXPENDITURE.getClassifyType()))
                .map(FundsRecordClassifyEntity::getClassifyId)
                .collect(Collectors.toList());


        FundsOverviewInfo.Overview overview = sumOverview(null, null, incomeClassify, expenditureClassify);

        // year
        LocalDate today = LocalDate.now();
        LocalDateTime startYear = LocalDateTime.of(LocalDate.of(today.getYear(), 1, 1), LocalTime.MIN);
        FundsOverviewInfo.Overview yearOverview = sumOverview(startYear, null, incomeClassify, expenditureClassify);

        // month
        LocalDateTime startMonth = LocalDateTime.of(today.withDayOfMonth(1), LocalTime.MIN);
        FundsOverviewInfo.Overview monthOverview = sumOverview(startMonth, null, incomeClassify, expenditureClassify);

        // week
        LocalDateTime monday = LocalDateTime.of(today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)), LocalTime.MIN);
        FundsOverviewInfo.Overview weekOverview = sumOverview(monday, null, incomeClassify, expenditureClassify);

        // asserts, list all debit card
        List<AccountEntity> accountEntities = accountRepository.findAccountEntities();
        long asserts = accountEntities.stream()
                .filter(account -> Objects.equals(account.getAccountType(),
                        AccountTypeEnum.DEBIT_ACCOUNT.getAccountType()))
                .map(AccountEntity::getAccountBalance).flatMapToLong(LongStream::of)
                .sum();

        // liabilities , list all credit card
        long liabilities = accountEntities.stream()
                .filter(account -> Objects.equals(account.getAccountType(),
                        AccountTypeEnum.CREDIT_ACCOUNT.getAccountType()))
                .map(AccountEntity::getAccountBalance).flatMapToLong(LongStream::of)
                .sum();

        return FundsOverviewInfo.builder()
                .overview(overview)
                .yearOverview(yearOverview)
                .monthOverview(monthOverview)
                .weekOverview(weekOverview)
                .assets(asserts)
                .liabilities(liabilities)
                .build();
    }

    @Override
    public List<FundsTrendInfo> fundsTrend(TrendAnalyzeEnum trendType) {
        BaseAnalyzeStrategy analyzeService = AnalyzeServiceFactory.matchAnalyzeStrategy(trendType);

        return analyzeService.trendAnalyze();
    }

    @Override
    public FundsComposeInfo fundsCompose(TrendAnalyzeEnum trendType) {
        BaseAnalyzeStrategy analyzeService = AnalyzeServiceFactory.matchAnalyzeStrategy(trendType);

        return analyzeService.composeAnalyze();
    }

    @Override
    public FundsRecordTopInfo fundsTops(TrendAnalyzeEnum trendType) {
        BaseAnalyzeStrategy analyzeService = AnalyzeServiceFactory.matchAnalyzeStrategy(trendType);

        return analyzeService.fundsTops();
    }

    private FundsOverviewInfo.Overview sumOverview(LocalDateTime startTime, LocalDateTime endTime,
                                                   List<Long> incomeClassify, List<Long> expenditureClassify) {
        Long expenditure = complexAnalyzeRepository.sumOverview(expenditureClassify, startTime, endTime);
        Long income = complexAnalyzeRepository.sumOverview(incomeClassify, startTime, endTime);
        return FundsOverviewInfo.Overview.builder()
                .income(income)
                .expenditure(expenditure)
                .build();
    }

}
