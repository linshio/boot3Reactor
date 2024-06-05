package cn.linshio.converter;


import cn.linshio.entity.TAuthor;
import cn.linshio.entity.TBook;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Instant;

/**
 * @author linshio
 * @create 2024/6/5 15:45
 */
@ReadingConverter// 读取数据库数据的时候，把row转成 TBook
public class BookConverter implements Converter<Row, TBook> {
    @Override
    public TBook convert(Row source) {
        //自定义封装结果集
        TBook tBook = new TBook();
        tBook.setId(source.get("id",Long.class));
        tBook.setTitle(source.get("title",String.class));
        tBook.setAuthorId(source.get("author_id",Long.class));
        tBook.setPublishTime(source.get("publish_time", Instant.class));
        TAuthor tAuthor = new TAuthor();
        tAuthor.setName(source.get("name",String.class));
        tBook.setAuthor(tAuthor);
        return tBook;
    }
}
