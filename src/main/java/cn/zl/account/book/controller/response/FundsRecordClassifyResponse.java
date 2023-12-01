package cn.zl.account.book.controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lin.zl
 */
@Data
@Builder
public class FundsRecordClassifyResponse {

    @JsonSerialize(using= ToStringSerializer.class)
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


    private String classifyTypeName;

    /**
     * 分类描述
     */
    private String classifyDescribe;

    /**
     * 创建时间
     */
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
