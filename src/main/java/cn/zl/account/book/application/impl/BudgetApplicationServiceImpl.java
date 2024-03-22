package cn.zl.account.book.application.impl;

import cn.zl.account.book.application.BudgetApplicationService;
import cn.zl.account.book.architecture.BizException;
import cn.zl.account.book.domain.BudgetDomainService;
import cn.zl.account.book.domain.converter.BudgetEntityConverter;
import cn.zl.account.book.enums.ResponseStatusEnum;
import cn.zl.account.book.info.BudgetDetailInfo;
import cn.zl.account.book.info.BudgetInfo;
import cn.zl.account.book.infrastructure.entity.BudgetEntity;
import cn.zl.account.book.infrastructure.repository.BudgetRepository;
import cn.zl.account.book.util.SnowIdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author lin.zl
 */
@Service
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

    @Resource
    private BudgetDomainService budgetDomainService;

    @Resource
    private BudgetRepository budgetRepository;

    @Override
    public void createBudget(BudgetInfo budgetInfo) {
        BudgetEntity budgetEntity = BudgetEntityConverter.converter2Entity(budgetInfo);

        budgetEntity.setBudgetId(SnowIdUtils.nextId());
        budgetEntity.setCreateTime(LocalDateTime.now());
        budgetEntity.setModifyTime(LocalDateTime.now());

        budgetDomainService.createBudget(budgetEntity);
    }

    @Override
    public void removeBudget(Long budgetId) {

        BudgetEntity budgetEntity = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BizException(ResponseStatusEnum.BUDGET_NONE_EXIST));

        budgetDomainService.removeBudget(budgetEntity);
    }

    @Override
    public void modifyBudget(BudgetInfo budgetInfo) {
        BudgetEntity budgetEntity = BudgetEntityConverter.converter2Entity(budgetInfo);

        budgetEntity.setBudgetId(budgetInfo.getBudgetId());
        budgetEntity.setModifyTime(LocalDateTime.now());

        budgetDomainService.modifyBudget(budgetEntity);
    }

    @Override
    public BudgetDetailInfo findBudget(Long budgetId) {
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BizException(ResponseStatusEnum.BUDGET_NONE_EXIST));

        BudgetDetailInfo detailInfo = BudgetEntityConverter.converter2DetailInfo(budgetEntity);




        return null;
    }
}
