package cn.zl.account.book.controller.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsRecordClassifyResponseDTO {

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

    /**
     * 父分类ID，可以为空
     */
    private Long parentClassifyId;

    private String parentClassifyName;
}
