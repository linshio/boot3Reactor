package cn.linshio.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
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


    @Test
    void context(){
        //支持Context的中间操作
        Flux.just(1,2,3)
                .transformDeferredContextual((flux,context)->{
                    System.out.println(flux);
                    System.out.println(context);
                    return flux.map(i->i+"==>"+context.get("prefix"));
                })
                //上游能够拿到下游的最近一次数据
                .contextWrite(Context.of("prefix","哈哈"))
                //ThreadLocal共享了数据 上游的所有人都能够看到 Context由下游传递给上游
                .subscribe(System.out::println);
    }

    @Test
    void parallelFlux(){
        Flux.range(1,1000000)
                .buffer(100)
                .parallel(8)
                .runOn(Schedulers.newParallel("yy"))
                .log()
                .flatMap(list->Flux.fromIterable(list))
                .collectSortedList(Integer::compareTo)
                .subscribe(System.out::println);
    }

    @Test
    void block(){
        //阻塞
        List<Integer> block = Flux.just(1, 2, 4)
                .map(i -> i + 10)
                .collectList()
                .block();

        System.out.println("block = " + block);
    }

    @Test
    void sinks() throws InterruptedException, IOException {
//        Sinks.many(); //发送Flux数据
//        Sinks.one(); // 发送Mono数据

        //sinks : 接收器，数据管道

//        Sinks.many().unicast();//   单播：这个管道只能绑定单个订阅者 （消费者）
//        Sinks.many().multicast();// 多播：这个管道可以绑定多个订阅者
//        Sinks.many().replay();//    重放：这个管道可以重放元素。是否给后来的订阅者把之前存在的数据发送给他

        Sinks.Many<Object> objectMany = Sinks.many()
                .multicast()
                .onBackpressureBuffer();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                objectMany.tryEmitNext("a-"+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        objectMany.asFlux()
                .subscribe(s-> System.out.println("s1=>"+s));

        objectMany.asFlux()
                .subscribe(s-> System.out.println("s2=>"+s));

        System.in.read();
    }

    @Test
    void retryAndTimeOut() throws IOException {
        //超时
        Flux.just(1)
                .delayElements(Duration.ofSeconds(3))
                .log()
                .timeout(Duration.ofSeconds(2))
                .retry() //把流从头到尾再重新请求一次
                .subscribe();

        System.in.read();
    }


    @Test
    void onErrorContinue(){
        //onErrorContinue当发生错误的时候会继续
        Flux.just(1,2,3,0,5)
                .map(i->10/i)
                .onErrorContinue((err,value)->{
                    System.out.println("err = " + err);
                    System.out.println("value = " + value);
                    System.out.println("发生了错误，已经记录好了");
                }).subscribe(v-> System.out.println("v = " + v),
                        err-> System.out.println("err = " + err),
                        ()-> System.out.println("流正常结束"));
    }

    class BusinessException extends RuntimeException{
        public BusinessException(String msg){
            super(msg);
        }
    }

    @Test
    void onErrorResume(){
        Flux.just(1,2,4,0,5)
                .map(integer -> "100 / "+integer+" = "+(100/integer))
                .onErrorResume(err->Flux.error(new BusinessException(err.getMessage()+"炸了")))
                .subscribe(v-> System.out.println("v = " + v),
                        err-> System.out.println("err = " + err),
                        ()-> System.out.println("流正常结束"));

        Flux.just(1,2,4,0,5)
                .map(integer -> "100 / "+integer+" = "+(100/integer))
                .onErrorMap(err->new BusinessException(err.getMessage()+"又炸了"))
                .subscribe(v-> System.out.println("v = " + v),
                        err-> System.out.println("err = " + err),
                        ()-> System.out.println("流正常结束"));
    }


    //默认：错误是一种中断的行为
    @Test
    void onErrorReturn(){
        Flux.just(1,2,4,0,5)
                .map(integer -> "100 / "+integer+" = "+(100/integer))
                //吃掉异常，让消费者无法感知 并且返回一个默认值
//                .onErrorReturn("haha -> stop")
                //这里也可以对指定的异常进行感知 并且返回一个默认值
                .onErrorReturn(ArithmeticException.class,"haha -> stop")
                .subscribe(v-> System.out.println("v = " + v),
                        err-> System.out.println("err = " + err),
                        ()-> System.out.println("流正常结束"));
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
                    String ss = split[0] + " " + split[1];
                    return Flux.just(ss);
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
