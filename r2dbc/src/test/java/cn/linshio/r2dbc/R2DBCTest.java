package cn.linshio.r2dbc;

import cn.linshio.dao.AuthorDao;
import cn.linshio.dao.BookDao;
import cn.linshio.entity.TAuthor;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author linshio
 * @create 2024/6/5 9:59
 */
@SpringBootTest
public class R2DBCTest {

    //单表查询使用
    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    //复杂查询使用
    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private BookDao bookDao;


    @Test
    void bookDao() throws IOException {
        bookDao.findAll()
                        .subscribe(System.out::println);

        System.in.read();
    }

    @Test
    void authorDao() throws IOException {
        //查找所有的数据
//        authorDao.findAll()
//                .subscribe(System.out::println);

//        authorDao.findAllByIdInAndNameLike(Arrays.asList(1L,2L),"张%")
//                .subscribe(System.out::println);
        authorDao.findHaha()
                        .subscribe(System.out::println);

        System.in.read();
    }

    @Test
    void databaseClient() throws IOException {
        databaseClient.sql("select * from t_author where id = ?")
                .bind(0,1L)
                .fetch()//抓取数据
                .all()//返回所有数据
                .map(map -> {
                    String id = map.get("id").toString();
                    String name = map.get("name").toString();
                    return new TAuthor(Long.parseLong(id),name);
                }).subscribe(tAuthor -> System.out.println("tAuthor = " + tAuthor));
        System.in.read();
    }

    @Test
    void r2dbcEntityTemplate() throws IOException {
        //1、构造查询条件
        Criteria criteria = Criteria.empty().and("id").is(2L);
        //2、封装为Query对象
        Query query = Query.query(criteria);
        r2dbcEntityTemplate.select(query, TAuthor.class)
                .subscribe(System.out::println);

        System.in.read();
    }

    @Test
    void connection() throws IOException {
        //准备连接数据库的配置项
        MySqlConnectionConfiguration build = MySqlConnectionConfiguration.builder()
                .host("60.204.128.243")
                .username("root")
                .password("LingXi666")
                .database("test")
                .build();


        //1、获取连接工厂
        MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(build);
        //2、获取到连接 发送SQL

        //3、数据发布者
        Mono.from(connectionFactory.create())
                .flatMapMany(connection ->
                        connection.createStatement("select * from t_author where id=?")
                        .bind(0,2L)
                        .execute())
                .flatMap(result -> {
                    return result.map(readable -> {
                        Long id = readable.get("id", Long.class);
                        String name = readable.get("name", String.class);
                        return new TAuthor(id,name);
                    });
                }).subscribe(System.out::println);

        System.in.read();
    }
}
