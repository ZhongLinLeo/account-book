package cn.zl.account.book.application.impl;

import cn.zl.account.book.application.BudgetApplicationService;
import cn.zl.account.book.architecture.BizException;
import cn.zl.account.book.domain.BudgetDomainService;
import cn.zl.account.book.domain.converter.BudgetEntityConverter;
import cn.zl.account.book.domain.impl.RelBudgetClassifyDomainServiceImpl;
import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.info.BudgetDetailInfo;
import cn.zl.account.book.info.BudgetInfo;
import cn.zl.account.book.infrastructure.entity.BudgetEntity;
import cn.zl.account.book.infrastructure.repository.BudgetRepository;
import cn.zl.account.book.infrastructure.repository.ComplexAnalyzeRepository;
import cn.zl.account.book.util.SnowIdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lin.zl
 */
@Service
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

    @Resource
    private BudgetDomainService budgetDomainService;

    @Resource
    private BudgetRepository budgetRepository;

    @Resource
    private RelBudgetClassifyDomainServiceImpl relBudgetClassifyDomainService;

    @Resource
    private ComplexAnalyzeRepository complexAnalyzeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBudget(BudgetInfo budgetInfo) {
        // create budget
        BudgetEntity budgetEntity = BudgetEntityConverter.converter2Entity(budgetInfo);

        long budgetId = SnowIdUtils.nextId();
        budgetEntity.setBudgetId(budgetId);
        budgetEntity.setCreateTime(LocalDateTime.now());
        budgetEntity.setModifyTime(LocalDateTime.now());
        budgetDomainService.createBudget(budgetEntity);

        // create budget and classify relation
        relBudgetClassifyDomainService.createRelBudgetClassify(budgetId, budgetInfo.getClassifyIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBudget(Long budgetId) {
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BizException(ResponseStatusEnum.BUDGET_NONE_EXIST));

        budgetDomainService.removeBudget(budgetEntity);

        relBudgetClassifyDomainService.removeRelation(budgetId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyBudget(BudgetInfo budgetInfo) {
        BudgetEntity budgetEntity = BudgetEntityConverter.converter2Entity(budgetInfo);

        final Long budgetId = budgetInfo.getBudgetId();
        budgetEntity.setBudgetId(budgetId);
        budgetEntity.setModifyTime(LocalDateTime.now());

        budgetDomainService.modifyBudget(budgetEntity);

        // modify budget and classify relation
        relBudgetClassifyDomainService.modifyRelBudgetClassify(budgetId, budgetInfo.getClassifyIds());
    }

    @Override
    public BudgetDetailInfo findBudget(Long budgetId) {
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BizException(ResponseStatusEnum.BUDGET_NONE_EXIST));

        BudgetDetailInfo detailInfo = BudgetEntityConverter.converter2DetailInfo(budgetEntity);

        List<Long> classifyIds = relBudgetClassifyDomainService.listRelBudgetClassify(budgetId);

        // 查询改预算范围内已使用的金额
        final Long usedAmount = complexAnalyzeRepository
                .sumOverview(classifyIds, budgetEntity.getBudgetStart(), budgetEntity.getBudgetEnd());
        detailInfo.setUsedAmount(usedAmount);

        return detailInfo;
    }
}
