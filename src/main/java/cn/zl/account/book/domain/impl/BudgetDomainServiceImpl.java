package cn.zl.account.book.domain.impl;

import cn.zl.account.book.domain.BudgetDomainService;
import cn.zl.account.book.infrastructure.entity.BudgetEntity;
import cn.zl.account.book.infrastructure.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author lin.zl
 */
@Service
public class BudgetDomainServiceImpl implements BudgetDomainService {

    @Resource
    private BudgetRepository budgetRepository;

    @Override
    public void createBudget(BudgetEntity budgetEntity) {
        budgetRepository.save(budgetEntity);
    }

    @Override
    public void removeBudget(BudgetEntity budgetEntity) {
        budgetEntity.setInvalid(1);
        budgetRepository.save(budgetEntity);
    }

    @Override
    public void modifyBudget(BudgetEntity budgetEntity) {
        budgetRepository.save(budgetEntity);
    }
}
