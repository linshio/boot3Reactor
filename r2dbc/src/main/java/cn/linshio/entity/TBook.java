package cn.linshio.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * @author linshio
 * @create 2024/6/5 14:23
 */
@Table("t_book")
@Data
    public class TBook {

    @Id
    private Long id;

    private String title;

    private Long authorId;

    //响应式中的日期应该使用Instant 或者 LocalXxx
    private Instant publishTime;

    //每本书都会有一本唯一的作者
    private TAuthor author;
}
