package cn.zl.account.book.infrastructure.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author create by leo.zl on 2023/8/18
 */
@Data
@MappedSuperclass
public class EntityBase {

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    private Integer invalid =0;
}
