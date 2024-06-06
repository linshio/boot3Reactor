package cn.linshio.config;


import cn.linshio.component.AppReactiveUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author linshio
 * @create 2024/6/6 11:42
 */
@Configuration
public class AppSecurityConfiguration {

    @Autowired
    private AppReactiveUserDetailsService reactiveUserDetailsService;

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        //定义那些请求需要认证，哪些请求不需要认证
        http.authorizeExchange(authorize -> {
            //静态资源允许所有人都能访问
            authorize.pathMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll();
            //剩下的所有请求都需要认证
            authorize.anyExchange().authenticated();
        });

        //开启默认的表单登录
        http.formLogin();


        //安全控制
        http.csrf(csrfSpec -> {
            csrfSpec.disable();
        });

        //配置认证规则
        //UserDetailsRepositoryReactiveAuthenticationManager 用户信息去数据库中查找
        //需要传入ReactiveUserDetailsService 响应式用户查询

        http.authenticationManager(new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService));
        //构建出安全配置
        return http.build();
    }


    @Bean
    PasswordEncoder myPasswordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
