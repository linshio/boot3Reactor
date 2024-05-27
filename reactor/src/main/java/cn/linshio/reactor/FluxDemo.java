package cn.linshio.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

/**
 * ClassName: FluxDemo
 * Package: cn.linshio.reactor
 * Description:
 *
 * @Author Linshio
 * @Create 2024/5/27 21:30
 */
public class FluxDemo {


    public static void main(String[] args) {
        // Mono : 0|1个元素的流
        Mono<Integer> just = Mono.just(1);
        just.subscribe(System.out::println);
    }

    public static void flux(String[] args) throws IOException {

        // Mono : 0|1个元素的流
        // Flux : N个元素的流
        //发布者发布数据流：源头

        //多元素的流
        Flux<Integer> just = Flux.just(1, 2, 4, 5, 6);

        //流不消费就不会用上
        just.subscribe(System.out::println);

        //一个数据流可以有很多的消费者
        just.subscribe(System.out::println);

        //对于每个消费者来说都是一样的  广播模式
        System.out.println("================");
        //每一秒打印一个数字
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        interval.subscribe(System.out::println);

        //由于线程是异步的，想要看到效果的话需要将主线程卡住后进行操作
        System.in.read();

    }
}
