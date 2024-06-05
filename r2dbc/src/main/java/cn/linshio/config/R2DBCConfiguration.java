package cn.linshio.config;

import cn.linshio.converter.BookConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * @author linshio
 * @create 2024/6/5 13:46
 */
@Configuration
@EnableR2dbcRepositories//开启 R2dbc的仓库功能 jpa
public class R2DBCConfiguration {

    @Bean
    public R2dbcCustomConversions conversions(){
        //把我们的转换器加入进去
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE,new BookConverter());
    }
}
