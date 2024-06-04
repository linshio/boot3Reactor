package cn.linshio.webFlux.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author linshio
 * @create 2024/6/4 14:13
 */
@Component
public class MyWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        System.out.println("目标方法放行之前...");
        //放行
        Mono<Void> filter = chain.filter(exchange);
        filter.doFinally((signalType)->{
            System.out.println("目标方法放行之后..."+signalType);
        });
        return filter;
    }
}
