package cn.linshio.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
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
        //subscribe 订阅流 ：没订阅之前流什么也不做

        Flux<String> map = Flux.range(1, 10)
                .map(integer -> "haha" + integer);

        // 流只是被订阅 无参的只是将该数据拿来什么也不做 空订阅
        map.subscribe();
        //流被消费 正常消费者 只消费正常元素
        map.subscribe(System.out::println);

    }


    public static void FluxMono(String[] args) {
        //concat 合并流
//        Flux.concat(Flux.just(1,2,3),Flux.just(4,5,6))
//                .subscribe(System.out::println);
        Flux.range(1,7)
                .log()
                .filter(integer -> integer>3)
                .log()
                .map(integer -> "haha->"+integer)
                .log()
                .subscribe(System.out::println);

    }


    public static void doOn(String[] args) throws IOException {
        // Mono : 0|1个元素的流
        Mono<Integer> just = Mono.just(1);
        just.subscribe(System.out::println);

        System.out.println("===============");
        //事件感知API  当流发生什么事情的时候能够触发一个回调；系统提前定义好的钩子函数 doOnXXX

        /**
         *  1.doOnNext :每个数据（流的数据）到达的时候进行触发
         *  2.doOnEach :每个元素（流的数据和信号）到达的时候进行触发
         *  3.doOnRequest:消费者请求流元素的时候
         *  4.doOnError:流发生错误
         *  5.doOnSubscribe:流被订阅的时候
         *  6.doOnTerminate:发送取消/预异常信号中断了流
         *  7.doOnCancel :流中元素被取消的时
         *  8.doOnDiscard:流中元素被忽略的时候
         */
        Flux<Integer> justed = Flux.range(1, 7)
                .delayElements(Duration.ofSeconds(1))
                .doOnComplete(()-> System.out.println("流正常结束"))
                .doOnCancel(()-> System.out.println("流已经被取消"))
                ;

        justed.subscribe(System.out::println);

        System.in.read();

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
