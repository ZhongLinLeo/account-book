package cn.zl.account.book.application.info;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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

    private LocalDateTime createTime;

    /**
     * 分类图标
     */
    private String classifyIcon;

    /**
     * 默认分类
     */
    private Integer defaultClassify;

    /**
     * 是否计入收支分析
     */
    private Integer includeAnalyze;
}
