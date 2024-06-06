package cn.linshio.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author linshio
 * @create 2024/6/6 13:43
 */
@Component //来定义如何去数据库中按照用户名查询到用户信息
public class AppReactiveUserDetailsService implements ReactiveUserDetailsService {


    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    PasswordEncoder passwordEncoder;

    //自定义如何去根据用户名查询到用户的相关信息
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        //从数据库中查询到用户、角色、权限所有的信息
        return databaseClient.sql("select t_user.*,t_roles.id rid,t_roles.name,t_roles.value,t_perm.id pid,t_perm.value pvalue,t_perm.description from t_user  " +
                        "left join t_user_role on t_user.id = t_user_role.user_id " +
                        "left join t_roles on t_user_role.role_id = t_roles.id " +
                        "left join t_role_perm on t_role_perm.role_id = t_roles.id " +
                        "left join t_perm on t_perm.id = t_role_perm.perm_id " +
                        "where t_user.username = ? limit 1")
                .bind(0, username)
                .fetch()
                .one()//all
                .map(map -> {
                    return User.builder()
                            .username(username)
                            .password(map.get("password").toString())
//                            .passwordEncoder(str->passwordEncoder.encode(str))
                            .authorities(new SimpleGrantedAuthority("download"))//权限
                            .roles("admin", "sale")
                            .build();

                });
    }
}
