package cn.linshio.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * ClassName: FunctionDemo
 * Package: cn.linshio.lambda
 * Description:
 *
 * @Author Linshio
 * @Create 2024/5/26 10:16
 */

public class FunctionDemo {
    public static void main(String[] args) {
        //生成数据
        Supplier<String> supplier = ()->"41";
        //验证数据
        Predicate<String> predicate = s -> s.matches("-?\\d+(\\.\\d+)?");
        //转换数据
        Function<String,Integer> function = Integer::parseInt;
        //消费数据
        Consumer<Integer> consumer = num -> {
            if (num%2==0){
                System.out.println("该数为偶数==>"+num);
            }else {
                System.out.println("该数为奇数==>"+num);
            }
        };

//        extracted(predicate, supplier, consumer, function);
        extracted(s->s.matches("-?\\d+(\\.\\d+)?"),
                ()->"41",
                num->{if (num%2==0){
                    System.out.println("该数为偶数==>"+num);
                }else {
                    System.out.println("该数为奇数==>"+num);
                }},
                Integer::parseInt);

    }

    private static void extracted(Predicate<String> predicate, Supplier<String> supplier, Consumer<Integer> consumer, Function<String, Integer> function) {
        boolean test = predicate.test(supplier.get());
        if (test){
            consumer.accept(function.apply(supplier.get()));
        }else {
            System.out.println("该数据不合法！");
        }
    }
}
