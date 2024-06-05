package cn.linshio.dao;

import cn.linshio.entity.TAuthor;
import cn.linshio.entity.TBook;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * @author linshio
 * @create 2024/6/5 13:51
 */
@Repository
public interface AuthorDao extends R2dbcRepository<TAuthor,Long> {

    //仅限单表复杂条件查询
    Flux<TAuthor> findAllByIdInAndNameLike(Collection<Long> id, String name);

    @Query("select * from t_author")//自定义query注解指定SQL语句
    Flux<TAuthor> findHaha();

    //1、一个图书有唯一的作者       1-1
    //2、一个作者可以有很多图书     1-N

    @Query("select b.*,a.name as name from t_book b" +
            "left join t_author a" +
            "on a.id = b.author_id" +
            "where b.id = :bookId")
    Mono<TBook> findBookAndAuthor(@Param("bookId") Long bookId);
}
