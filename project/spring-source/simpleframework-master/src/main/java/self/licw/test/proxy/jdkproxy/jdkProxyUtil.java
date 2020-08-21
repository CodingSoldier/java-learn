package self.licw.test.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class jdkProxyUtil {
    //创建被代理类的代理类实例
    public static <T> T newProxyInstance(T targetObject, InvocationHandler invocationHandler){
        //从被代理类中获得类加载器
        ClassLoader classLoader = targetObject.getClass().getClassLoader();
        //获得被代理类的所有接口（代理类也就能代理这个类的这些接口的所有方法）
        Class<?>[] interfaces = targetObject.getClass().getInterfaces();
        //新建代理类Proxy的三个参数 类加载器，被代理类的接口，实现了invocationHandler接口的动态代理类（这里可以直接传入invocationHandler接口，更方便）
        return (T) Proxy.newProxyInstance(classLoader,interfaces,invocationHandler);
    }
}
