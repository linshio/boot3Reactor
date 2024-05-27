package cn.linshio.flow;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author linshio
 * @create 2024/5/27 9:33
 */

public class FlowDemo {

    //定义一个流中间操作处理器  其中的这个流式操作处理器 可以理解为 即是一个订阅者 也是一个发布者
    static class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<String,String>{

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("process订阅绑定完成");
            this.subscription = subscription;
            subscription.request(1);//找上游要一个数据
        }

        @Override
        public void onNext(String item) {
            System.out.println("process拿到数据"+item);
            //再加工
            item+="：哈哈";

            //在此处需要将加工后的数据发出去
            submit(item);

            //将这个数据处理完之后需要再要一个数据回调当前函数进行数据处理
            subscription.request(1);//找上游要一个数据
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }


    /**
     * 1.publisher发布者
     * 2.subscriber订阅者
     * 3.subscription订阅关系
     * 4.processor:处理器
     */

    public static void main(String[] args) throws InterruptedException {

        //1定义一个发布者发布数据到 缓冲区
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();


        //定义一个中间操作 给每个元素的前缀加上一个  哈哈的前缀
        MyProcessor myProcessor = new MyProcessor();

        //2定义一个订阅者，订阅自己感兴趣的资源
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {

            //subscription： 发布者与订阅者之间的订阅关系
            private Flow.Subscription subscription;
            //触发的事件 在订阅的时候 OnXxx 表示在xxx事件发生的时候调用这个回调函数
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.println(Thread.currentThread()+"订阅开始了==>"+subscription);
                this.subscription = subscription;
                //向上游请求数据
                subscription.request(1);
            }

            //在下一个元素到达的时候 执行这个回调
            @Override
            public void onNext(String item) {
                System.out.println(Thread.currentThread()+"订阅者接受到数据==>"+item);
                //向上游请求数据
//                subscription.request(1);
                if ("p-7".equals(item)){
                    subscription.cancel();//取消订阅
                }else {
                    subscription.request(1);
                }
            }

            //在错误发生的时候
            @Override
            public void onError(Throwable throwable) {
                System.out.println(Thread.currentThread()+"订阅者接受到错误信号==>"+throwable);
            }
            //在完成的时候
            @Override
            public void onComplete() {
                System.out.println(Thread.currentThread()+"<=订阅者接受到完成信号=>");
            }
        };

        //3绑定发布者与订阅者
//        publisher.subscribe(subscriber);
        //此处的处理器相当于订阅者
        publisher.subscribe(myProcessor);
        //此处的处理器相当于发布者
        myProcessor.subscribe(subscriber);


        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread());
            //发布10条数据 到缓冲区
            publisher.submit("p-"+i);
            //异常中断
//            publisher.closeExceptionally(new RuntimeException());
        }

        //jvm底层对于整个发布订阅关系做好了 异步+缓存区处理 = 响应式系统

        //发布者通道关闭
        publisher.close();
        Thread.sleep(10000);
    }
}
