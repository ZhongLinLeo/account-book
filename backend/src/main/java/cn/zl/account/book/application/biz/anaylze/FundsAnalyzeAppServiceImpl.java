package cn.zl.account.book.application.biz.anaylze;

import cn.zl.account.book.application.info.FundsOverviewInfo;
import cn.zl.account.book.controller.application.FundsAnalyzeAppService;
import cn.zl.account.book.controller.enums.ClassifyTypeEnum;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * @author lin.zl
 */
@Service
@Slf4j
public class FundsAnalyzeAppServiceImpl implements FundsAnalyzeAppService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

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

        // todo liabilities an asserts

        return FundsOverviewInfo.builder()
                .overview(overview)
                .yearOverview(yearOverview)
                .monthOverview(monthOverview)
                .weekOverview(weekOverview)
                .build();
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
