package cn.zl.account.book.domain.impl;

import cn.zl.account.book.domain.RelBudgetClassifyDomainService;
import cn.zl.account.book.infrastructure.entity.RelBudgetClassifyEntity;
import cn.zl.account.book.infrastructure.repository.RelBudgetClassifyRepository;
import cn.zl.account.book.util.SnowIdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@Service
public class RelBudgetClassifyDomainServiceImpl implements RelBudgetClassifyDomainService {

    @Resource
    private RelBudgetClassifyRepository relBudgetClassifyRepository;

    @Override
    public void createRelBudgetClassify(Long budgetId, Set<Long> classifyIds) {
        Set<RelBudgetClassifyEntity> entities = classifyIds.stream()
                .map(classifyId -> {
                    RelBudgetClassifyEntity entity = new RelBudgetClassifyEntity();
                    entity.setId(SnowIdUtils.nextId());
                    entity.setBudgetId(budgetId);
                    entity.setClassifyId(classifyId);
                    return entity;
                })
                .collect(Collectors.toSet());

        relBudgetClassifyRepository.saveAllAndFlush(entities);
    }

    @Override
    public void removeRelation(Long budgetId) {
        relBudgetClassifyRepository.removeRelBudgetClassifyEntitiesByBudgetId(budgetId);
    }
}
