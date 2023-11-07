package cn.zl.account.book.application.biz.anaylze;

import cn.zl.account.book.application.enums.AccountTypeEnum;
import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.application.info.FundsTrendInfo;
import cn.zl.account.book.controller.application.FundsAnalyzeAppService;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.infrastructure.biz.account.AccountRepository;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;
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
    private AccountRepository accountRepository;

    @Override
    public FundsOverviewInfo fundsOverview() {
        FundsOverviewInfo.Overview overview = sumOverview(null);

        // year
        LocalDate today = LocalDate.now();
        LocalDate firstDatOfYear = today.withDayOfYear(1);
        FundsOverviewInfo.Overview yearOverview = sumOverview(firstDatOfYear);

        // month
        LocalDate firstDatOfMonth = today.withDayOfMonth(1);
        FundsOverviewInfo.Overview monthOverview = sumOverview(firstDatOfMonth);

        // week
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        FundsOverviewInfo.Overview weekOverview = sumOverview(monday);

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
    public List<FundsTrendInfo> fundsTrend(String trendType) {
        return null;
    }

    private FundsOverviewInfo.Overview sumOverview(LocalDate firstDatOfYear) {
        Long income = fundsRecordRepository.sumOverview(ClassifyTypeEnum.INCOME.getClassifyType(), firstDatOfYear);
        Long expenditure = fundsRecordRepository.sumOverview(ClassifyTypeEnum.OUTCOME.getClassifyType(), firstDatOfYear);
        return FundsOverviewInfo.Overview.builder()
                .income(income)
                .expenditure(expenditure)
                .build();
    }

}
