package projectnote.spring.aoptheory;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/4
 */
public class A {
    // 定义一个简单方法，模拟应用中的业务逻辑方法
    public void sayHello(){System.out.println("Hello AspectJ!");}
    // 主方法，程序的入口
    public static void main(String[] args)
    {
        A a = new A();
        a.sayHello();
    }
}
