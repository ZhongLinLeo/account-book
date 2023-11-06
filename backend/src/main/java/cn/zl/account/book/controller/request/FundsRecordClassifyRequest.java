package cn.zl.account.book.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lin.zl
 */
@Data
public class FundsRecordClassifyRequest {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String classifyName;

    /**
     * 分类类型
     * 0:支出
     * 1:收入
     */
    @NotNull(message = "分类名称不能为空")
    private Integer classifyType;

    /**
     * 分类描述
     */
    private String classifyDescribe;

    /**
     * 分类图标
     */
    private String classifyIcon;
}
