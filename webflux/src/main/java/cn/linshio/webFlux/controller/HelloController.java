package cn.linshio.webFlux.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author linshio
 * @create 2024/6/4 9:14
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "key",required = false,defaultValue = "hello") String key){

        return "Hello ==> key=" + key;
    }

    //返回单个数据使用Mono
    //返回多个数据使用Flux
    //配合Flux，完成SSE：Server Send Event;服务端事件推送
    @GetMapping("/haha")
    public Mono<String> haha(){
        return Mono.just(5)
                .map(i->10/i)
                .map(i->"haha-"+i);

    }

    @GetMapping("/hehe")
    public Flux<String> hehe(){
        return Flux.just("hehe1","hehe2");
    }

    //sse测试  服务端推送
    @GetMapping(value = "/sse",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse(){
        return Flux.range(1,10)
                .map(i->{
                    return ServerSentEvent.builder("haha-" + i)
                            .id(i + "")
                            .comment("hehe-" + i)
                            .event("haha")
                            .build();
                })
                .delayElements(Duration.ofMillis(200));
    }

    /**
     * HandlerMapping       : 请求映射处理器；保存每个请求由哪个方法进行处理
     * HandlerAdapter       : 处理器适配器了；反射执行目标方法
     * HandlerResultHandler : 处理器结果 的 处理器
     */



}
