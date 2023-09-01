package cn.zl.account.book.controller.request;

import lombok.Data;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordClassifyRequest {

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

    /**
     * 父分类ID，可以为空
     */
    private Long parentClassifyId;
}
