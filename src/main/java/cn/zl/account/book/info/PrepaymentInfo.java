package cn.zl.account.book.info;

import lombok.Data;

import java.time.LocalDate;

/**
 * 提前还款信息
 *
 * @author lin.zl
 */
@Data
public class PrepaymentInfo {
    private Double prepaymentAmount;

    private LocalDate prepaymentDate;

    private Double prepayInterest;
}
