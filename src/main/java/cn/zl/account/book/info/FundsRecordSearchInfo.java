package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsRecordSearchInfo {

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 分类id列表
     */
    private List<Long> classifyIds;

    /**
     * 账户列表
     */
    private List<Long> accountIds;

    /**
     * 关键词
     */
    private String recordKeyword;

    public String getRecordKeyWord() {
        return StringUtils.isBlank(recordKeyword) ? null : "%" + recordKeyword + "%";
    }

    public List<Long> getClassifyIds() {
        return CollectionUtils.isEmpty(classifyIds) ? Collections.emptyList() : classifyIds;
    }

    public List<Long> getAccountIds() {
        return CollectionUtils.isEmpty(accountIds) ? Collections.emptyList() : accountIds;
    }
}
