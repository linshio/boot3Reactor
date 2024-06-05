package cn.linshio.dao;

import cn.linshio.entity.TBook;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @author linshio
 * @create 2024/6/5 14:27
 */
@Repository
public interface BookDao extends R2dbcRepository<TBook,Long> {
}
