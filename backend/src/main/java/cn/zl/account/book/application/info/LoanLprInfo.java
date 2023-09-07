package cn.zl.account.book.application.info;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author lin.zl
 */
@Data
public class LoanLprInfo {

    private LocalDate lprDate;

    private Double lpr;
}
