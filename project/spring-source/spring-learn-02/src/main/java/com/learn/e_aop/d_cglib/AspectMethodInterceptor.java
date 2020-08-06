package com.learn.e_aop.d_cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author chenpiqian
 * @date: 2020-08-06
 */
public class AspectMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object target, Method targetMethod, Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        Object result = methodProxy.invokeSuper(target, args);
        after();
        return result;
    }

    private void before(){
        System.out.println("方法执行前");
    }

    private void after(){
        System.out.println("方法执行后");
    }

}
