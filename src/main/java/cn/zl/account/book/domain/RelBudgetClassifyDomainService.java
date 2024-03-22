package cn.zl.account.book.domain;

import java.util.Set;

/**
 * @author lin.zl
 */
public interface RelBudgetClassifyDomainService {

    /**
     * 添加预算分类
     * @param budgetId budgetId
     * @param classifyIds classifyIds
     */
    void createRelBudgetClassify(Long budgetId, Set<Long> classifyIds);


    /**
     * 删除预算分类
     * @param budgetId budgetId
     */
    void removeRelation(Long budgetId);
}
