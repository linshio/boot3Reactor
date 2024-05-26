package cn.linshio.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: StreamDemo
 * Package: cn.linshio.lambda
 * Description:
 *
 * @Author Linshio
 * @Create 2024/5/26 10:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class Person{
    private String name;
    private String sex;
    private Integer age;
}


public class StreamDemo {
    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("张 三", "女", 15),
                new Person("李 四", "男", 18),
                new Person("万 无", "女", 24),
                new Person("遭 六", "男", 32)
        );

        //拿到集合流其实就是拿到集合的深拷贝中的值，流的所有操作都是流的元素引用
        people.stream()
                .filter(p->p.getAge()>=18)
                //peek 一般用来打印输出
                .peek(System.out::println)
                //map 一一映射
                .map(p->p.getName())
                //flatMap 一对多映射 返回值为一个新流
                .flatMap(p->{
                    String[] s = p.split(" ");
                    return Arrays.stream(s);
                })
                .distinct()
                .sorted(String::compareTo)
                .forEach(System.out::println);

        List<Integer> list = List.of(1, 2, 3, 4, 5, 6)
                .stream()
                //filter 会无条件的遍历流中的每一个元素
                .filter(ele->ele>=2)
                .collect(Collectors.toList());

        System.out.println("list = " + list);

        List<Integer> list1 = List.of(1, 2, 3, 4, 5, 6)
                .stream()
                //takeWhile 当满足条件的时候就拿到这个元素 不满足的时候直接结束该流的操作
                .takeWhile(ele->ele<=2)
                .collect(Collectors.toList());

        System.out.println("list1 = " + list1);


        Map<String, List<Person>> collect = people.stream()
                .filter(ele -> ele.getAge() >= 15)
                .collect(Collectors.groupingBy(ele -> ele.getSex()));

        System.out.println("collect = " + collect);
    }

    public static void aaa(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //拿到其中最大的偶数  进行输出打印
        list.stream()
                .filter(n->{
                    System.out.println("filter is done ==> "+n);
                    return n%2==0;
                })
                .max(Integer::compareTo)
                .ifPresent(System.out::println);

    }
}
