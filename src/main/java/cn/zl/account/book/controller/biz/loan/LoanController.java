package cn.zl.account.book.controller.biz.loan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("loan")
public class LoanController {

    @GetMapping("list")
    public void listLoanInfo() {

    }

}
