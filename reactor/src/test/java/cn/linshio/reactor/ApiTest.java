package cn.linshio.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: ApiTest
 * Package: cn.linshio.reactor
 * Description:
 *
 * @Author Linshio
 * @Create 2024/6/1 20:48
 */
public class ApiTest {


    //默认：错误是一种中断的行为
    @Test
    void error(){
        Flux.just(1,2,4,0,5)
                .map(integer -> "100 / "+integer+" = "+(100/integer))
                .subscribe(v-> System.out.println("v = " + v),
                        err-> System.out.println("err = " + err),
                        ()-> System.out.println("流结束"));
    }


    /**
     * filter flatMap concatMap concat concatWith
     * defaultIfEmpty switchIfEmpty transform transformDeferred  merge
     * mergeWith mergeSequential zip zipWith
     */

    @Test
    void zip(){
        Flux.just(1,2,3)
                .zipWith(Flux.just("a","b","c"))
                .map(tuple->{
                    //元组中第一个元素
                    Integer t1 = tuple.getT1();
                    //元组中第二个元素
                    String t2 = tuple.getT2();
                    return t1 +"-"+t2;
                })
                .log()
                .subscribe();
    }


    @Test
    void merge() throws IOException {
        /**
         * concat : 连接 A流中的所有元素与B流进行拼接
         * merge :  合并 A流中的元素 与 B流中的元素 按照时间的序列进行合并
         */
        Flux.merge(
                Flux.just(1,2,3).delayElements(Duration.ofMillis(1000)),
                Flux.just("a","b").delayElements(Duration.ofMillis(1500)),
                Flux.just("ha-ha","hei-hei").delayElements(Duration.ofMillis(500))
        ).log().subscribe();

        System.in.read();
    }


    @Test
    void switchIfEmpty(){
        Mono.empty()
                //如果发布者的元素为null 指定默认值，否则使用发布者的值
                //该方法参数为一个新流
                .switchIfEmpty(Mono.just("66"))
                .subscribe(System.out::println);
    }

    @Test
    void defaultIfEmpty(){
        Mono.empty()
                //如果发布者的元素为null 指定默认值，否则使用发布者的值
                //该方法参数为 值
                .defaultIfEmpty("2")
                .subscribe(System.out::println);
    }


    @Test
    void transformDeferred(){
        AtomicInteger atomic = new AtomicInteger(0);
        Flux<String> transform = Flux.just("a", "b", "c")
                .transformDeferred(v -> {
                    //++atomic
                    if (atomic.incrementAndGet() == 1) {
                        //如果是第一次调用就返回大写
                        return v.map(String::toUpperCase);
                    } else {
                        //如果不是第一次调用就直接返回
                        return v;
                    }
                });
        //transform 有defer会共享外部变量的值 有状态转换 无论有多少个订阅者，每个订阅者transform都会执行
        transform.subscribe(v-> System.out.println("one->"+v));
        transform.subscribe(v-> System.out.println("two->"+v));
    }


    @Test
    void transform(){
        AtomicInteger atomic = new AtomicInteger(0);
        Flux<String> transform = Flux.just("a", "b", "c")
                .transform(v -> {
                    //++atomic
                    if (atomic.incrementAndGet() == 1) {
                        //如果是第一次调用就返回大写
                        return v.map(String::toUpperCase);
                    } else {
                        //如果不是第一次调用就直接返回
                        return v;
                    }
                });
        //transform 无defer不会共享外部变量的值 无状态转换 无论有多少个订阅者，transform都会执行一次
        transform.subscribe(v-> System.out.println("one->"+v));
        transform.subscribe(v-> System.out.println("two->"+v));
    }

    @Test
    void concatWith(){
        Flux.just(1,2)
                .concatWith(Mono.just(6))
                .log()
                .subscribe();
    }

    @Test
    void concat(){
        Flux.concat(Flux.just(1,2),Flux.just("haha","hehe"))
                .log()
                .subscribe();
    }

    @Test
    void concatMap(){
        Flux.just(1,2)
                .concatMap(s->Flux.just(s+"->a"),1)
                .log()
                .subscribe();
    }

    @Test
    void flatMap(){
        Flux.just("zhang-san","li-si")
                .flatMap(s->{
                    String[] split = s.split("-");
                    return Flux.fromArray(split);
                })
                .log()
                .subscribe();
    }

    @Test
    void filter(){
        Flux.just(1,2,3,4,5,6)
                .filter(s->s%2==0)
                .log()
                .subscribe();
    }
}
