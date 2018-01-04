package com.spring.aop;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface A{
    int say();
}

class AImpl implements A{
    public int say(){
        System.out.println("T.say内容");
        return 1;
    }
}

/**
 * 使用jdk动态代理的目标对象必须是接口的实现类
 * http://www.cnblogs.com/jianjianyang/p/4904353.html
 */
class JdkDynamicProxy implements InvocationHandler{
    private Object proxyObj;

    public Object newProxyInstance(Object proxyObj){
        this.proxyObj = proxyObj;
        return Proxy.newProxyInstance(proxyObj.getClass().getClassLoader(),
                proxyObj.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("进入方法前");
        Object result = method.invoke(this.proxyObj, args);
        System.out.println("方法执行完后");
        return result;
    }
}

public class A_JdkProxy {
    @Test
    public void t(){
        JdkDynamicProxy jdkDynamicProxy = new JdkDynamicProxy();
        A a = new AImpl();
        a.say();
        a = (A)jdkDynamicProxy.newProxyInstance(a);
        int num = a.say();
        System.out.println(num);
    }
}
