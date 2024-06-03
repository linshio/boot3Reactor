package cn.linshio.webFlux;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

/**
 * ClassName: FluxMainApplicaiton
 * Package: cn.linshio.webFlux
 * Description:
 *
 * @Author Linshio
 * @Create 2024/6/3 21:19
 */
public class FluxMainApplication {
    public static void main(String[] args) {
        //1，创建一个能够处理Http请求的处理器
        HttpHandler handler = (ServerHttpRequest request, ServerHttpResponse response) -> {

            //编写业务逻辑

            return Mono.empty();
        };
        //2、启动一个服务器，监听8080端口，接受数据，并拿到数据交给HttpHandler进行请求处理
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        //3、启动Netty服务器
        HttpServer.create()
                .host("localhost")
                .port(8080)
                .handle(adapter)
                .bindNow();
    }
}
