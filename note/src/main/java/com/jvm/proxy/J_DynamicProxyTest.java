package com.jvm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//使用JDK生成代理对象，要定义的一个接口，代理对象也是此接口的实现类
interface HelloInterface {
    void sayHello();
}

//被代理类实现接口
class HelloImpl implements HelloInterface {
    @Override
    public void sayHello() {
        System.out.println("hello world");
    }
}

//动态代理类，用于生成代理对象
class DynamicProxy implements InvocationHandler {

    //本例中，被代理类是HelloImpl
    Object originalObj;

    //返回HelloImpl的代理对象$Proxy0.class的实例
    Object getProxy(Object originalObj) {
        this.originalObj = originalObj;
        return Proxy.newProxyInstance(originalObj.getClass().getClassLoader(),
                originalObj.getClass().getInterfaces(),
                this);
    }

    //代理对象$Proxy0.class的实例运行sayHello()，会调用此方法，此方法切入了前置、后置代码
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("切入前置代码，开始调用目标方法");
        Object proxyObj = method.invoke(originalObj, args);
        System.out.println("切入后置代码，目标方法已经调用结束");
        return proxyObj;
    }
}

public class J_DynamicProxyTest {

    public static void main(String[] args) {
        //在main方法中加入这句代码，程序运行时会在项目根目录生成一个名为$Proxy0.class的代理类Class文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        HelloInterface helloProxy = (HelloInterface) new DynamicProxy().getProxy(new HelloImpl());
        helloProxy.sayHello();

        System.out.println(helloProxy instanceof HelloImpl); //false，helloProxy不是HelloImpl的实例
        System.out.println(helloProxy.getClass().getName()); //com.jvm.proxy.$Proxy0

    }
}