package cn.linshio.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author linshio
 * @create 2024/6/6 11:30
 */
@RestController
@EnableReactiveMethodSecurity//开启到方法级别的权限控制
public class HelloController {


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/hello")
    public Mono<String> hello(){

        return Mono.just("hello");
    }

    @PreAuthorize("hasAuthority('haha')")
    @GetMapping("/world")
    public Mono<String> world(){
        return Mono.just("world");
    }
}
