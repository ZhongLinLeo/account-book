package cn.zl.account.book.infrastructure.bo;

/**
 * @author lin.zl
 */
//@Data
public interface AnalyzeTrendBo {

//    private Integer classifyType;
//
//    private LocalDate fundsRecordDate;
//
//    private Long totalFundsBalance;

    Integer getClassifyType();

    String getFundsRecordDate();

    Long getTotalFundsBalance();

}
