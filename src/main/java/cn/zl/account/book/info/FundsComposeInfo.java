package cn.zl.account.book.info;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

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
    public static class Compose {

        private String classifyName;

        private Set<String> classifyIds;

        private Double percent;

    }
}
