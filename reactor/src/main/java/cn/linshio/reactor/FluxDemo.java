package cn.linshio.reactor;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

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
        new FluxDemo().buffer();
    }

    private void buffer() {
        Flux<List<Integer>> buffer = Flux.range(1, 10)
                //缓冲区：缓冲3个元素：消费一次最多可以拿到3个元素
                .buffer(3);

        buffer.subscribe(new BaseSubscriber<List<Integer>>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                //请求一次数据  这里有缓冲区的话就是一次请求来三个数据
                request(1);
            }

            @Override
            protected void hookOnNext(List<Integer> value) {
                System.out.println("该数据为==>"+value);
                request(1);
            }
        });
    }


    public static void customerSubscribe(String[] args) {
        //subscribe 订阅流 ：没订阅之前流什么也不做


        //OnError XXX 、doOnXxx
        //doOnXxx ：发生这个事件的时候会产生一个回调、仅仅做一个通知处理、通知你（不能改变流）
        //onXxx  ： 发生这个事件后执行一个动作，可以改变元素、信号
        Flux<String> map = Flux.range(1, 10)
                .map(integer -> {
                    if (integer==9){
                        integer = integer/0;
                    }
                    return "haha" + integer;
                })
                //流错误的时候会把异常吃掉后转为正常的信号
                .onErrorComplete();

//        // 流只是被订阅 无参的只是将该数据拿来什么也不做 空订阅
//        map.subscribe();
////        //流被消费 正常消费者 只消费正常元素
//        map.subscribe(System.out::println);
//        //流被消费 正常消费 出现异常的情况进行异常处理
//        map.subscribe(System.out::println,e->e.printStackTrace());
//        //流被消费 正常消费 正常结束后会回调 complete
//        map.subscribe(System.out::println,e->e.printStackTrace(),()-> System.out.println("流正常结束了"));

        //自定义订阅者
        map.subscribe(new BaseSubscriber<String>() {
            @Override
            protected Subscription upstream() {
                return super.upstream();
            }

            //生命周期钩子1==>流被订阅的时候进行触发
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                //找发布者要一次请求数据 要完之后hookOnNext就会感知到下一个数据的到来
                //request(1)不是表示请求一个数据，而是发起一次请求
                request(1);

                //找发布者要所有的数据
//                requestUnbounded();
            }

            @Override
            protected void hookOnNext(String value) {
                System.out.println("数据到达==>"+value);
                request(1);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("流正常结束了...");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                System.out.println("流异常结束了..."+throwable.getMessage());
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("流被取消了...");
            }

            @Override
            protected void hookFinally(SignalType type) {
                System.out.println("最终回调..."+type);
            }
        });
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
