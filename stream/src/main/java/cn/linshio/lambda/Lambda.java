package cn.linshio.lambda;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.*;

/**
 * @author linshio
 * @create 2024/5/24 14:21
 */
//函数式接口：接口中有且只有一个未实现的方法
public class Lambda {


    public static void main(String[] args) {

        //提供者接口 无入参 有出参
        Supplier<String> supplier = ()-> UUID.randomUUID().toString();
        System.out.println(supplier.get());

        //function 无入参 无出参
        Runnable runnable = ()->{
            System.out.println("haha");
        };
        new Thread(runnable).run();

        //function 处理 有入参 有出参
        Function<String,Integer> function = x->Integer.parseInt(x);
        System.out.println(function.apply("3"));

        //消费者接口(消费两个 出一个)  有入参 无出参
        BiConsumer<String,String> biConsumer = (s1,s2)->{
            System.out.println(s1+"<<<>>>"+s2);
        };
        biConsumer.accept("hahah","hjeheh");

        //消费者接口(消费一个 出一个)  有入参 无出参
        Consumer<String> consumer = x-> System.out.println(x);
        consumer.accept("678");

        //断言型接口
        Predicate<Integer> predicate = n->n%2==0;
        //predicate.test(5) 正向判断
        //predicate.negate().test(5) 反向判断
        System.out.println("predicate.test() = " + predicate.negate().test(5));

    }

    public static void bbb(String[] args) {
        var strings = new ArrayList<String>();
        strings.add("Alice");
        strings.add("Bob");
        strings.add("Cry");
        strings.add("Dave");

//        Collections.sort(strings, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o2.compareTo(o1);
//            }
//        });

        Collections.sort(strings,((o1, o2) -> o2.compareTo(o1)));

        System.out.println(strings);
    }

    public static void aaa(String[] args) {
        //1.自己创建实现类对象进行接口的实现
        MyInterface myInterface = new MyImpl();
        System.out.println("myInterface.sum(1,2) = " + myInterface.sum(1, 2));

        //2.创建匿名实现类进行实现
        MyInterface myInterface1 = new MyInterface() {
            @Override
            public int sum(int i, int j) {
                return i*i + j*j;
            }
        };
        System.out.println("myInterface1 = " + myInterface1.sum(3,2));

        //3.lambda 表达式写法
        MyInterface myInterface2 = (i,j)->i+j;
        System.out.println("myInterface2.sum(5,9) = " + myInterface2.sum(5, 9));
    }
}
