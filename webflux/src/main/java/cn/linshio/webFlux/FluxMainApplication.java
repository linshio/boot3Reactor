package cn.linshio.webFlux;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;

/**
 * ClassName: FluxMainApplicaiton
 * Package: cn.linshio.webFlux
 * Description:
 *
 * @Author Linshio
 * @Create 2024/6/3 21:19
 */
public class FluxMainApplication {
    public static void aaa(String[] args) throws IOException {
        //1，创建一个能够处理Http请求的处理器  参数：请求】、响应；返回值：Mono<Void>
        HttpHandler handler = (ServerHttpRequest request, ServerHttpResponse response) -> {

            //编写业务逻辑
//            response.getHeaders();//获取请求头
//            response.getCookies();//获取Cookie
//            response.getStatusCode();//获取状态码
//            response.bufferFactory();//buffer 工厂
//            response.writeWith();//将信息写出
//            response.setComplete();//响应结束标志
            //创键响应数据的 DataBuffer
            DataBufferFactory factory = response.bufferFactory();
            //包装数据
            DataBuffer wrap = factory.wrap((request.getURI() + "<==>hello!").getBytes());
            //需要一个DataBuffer的发布者
            return response.writeWith(Mono.just(wrap));
        };
        //2、启动一个服务器，监听8080端口，接受数据，并拿到数据交给HttpHandler进行请求处理
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        //3、启动Netty服务器
        HttpServer.create()
                .host("localhost")
                .port(8080)
                .handle(adapter)
                .bindNow();//现在就绑定

        System.out.println("服务器启动完成...监听8080，并接受请求");
        System.in.read();
        System.out.println("服务器停止...");
    }
}
