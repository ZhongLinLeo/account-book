package cn.zl.account.book.application.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * lpr 信息
 *
 * @author lin.zl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanLprInfo {

    private LocalDate lprDate;

    private Double lpr;
}
