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
public class LoanResponse {

    @JsonSerialize(using= ToStringSerializer.class)
    private Long loanId;

    private String loanType;

    private String loanDesc;

    private String loanAmount;

    private Integer loanPeriod;

    private String repayAmount;

    private String repayDate;

    private String loanRemark;

    private Double loanRate;

}
