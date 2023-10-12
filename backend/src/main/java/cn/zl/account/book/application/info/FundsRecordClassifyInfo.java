package cn.zl.account.book.application.info;

import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsRecordClassifyInfo {

    private Long classifyId;

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 分类类型
     * 0:支出
     * 1:收入
     */
    private Integer classifyType;

    /**
     * 分类描述
     */
    private String classifyDescribe;

}
