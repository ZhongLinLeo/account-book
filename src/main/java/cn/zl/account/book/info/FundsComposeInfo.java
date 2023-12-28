package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 资金构成
 *
 * @author lin.zl
 */
@Data
@Builder
public class FundsComposeInfo {

    /**
     * 收入构成
     */
    private List<Compose> incomeCompose;

    /**
     * 支出构成
     */
    private List<Compose> expenditureCompose;

    @Data
    @Builder
    public static class Compose{

        private String classifyName;

        private Long classifyId;

        private Double percent;

    }
}
