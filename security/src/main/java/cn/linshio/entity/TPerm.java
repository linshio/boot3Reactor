package cn.linshio.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "t_perm")
@Data
public class TPerm {
    @Id
    private Long id;

    private String value;

    private String uri;

    private String description;

    private Instant createTime;

    private Instant updateTime;
}
