package cn.linshio.lambda;

/**
 * @author linshio
 * @create 2024/5/24 14:21
 */
//函数式接口：接口中有且只有一个未实现的方法
public class Lambda {

    public static void main(String[] args) {

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
