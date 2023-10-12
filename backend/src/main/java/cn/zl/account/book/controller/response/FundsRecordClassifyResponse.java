package cn.zl.account.book.controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

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

    /**
     * 分类描述
     */
    private String classifyDescribe;
}
