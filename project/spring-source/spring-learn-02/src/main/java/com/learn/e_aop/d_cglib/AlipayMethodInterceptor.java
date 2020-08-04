package com.learn.e_aop.d_cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class AlipayMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        beforePay();
        Object result = methodProxy.invokeSuper(o, args);
        afterPay();
        return result;
    }

    private void beforePay(){
        System.out.println("#######beforePay");
    }

    private void afterPay(){
        System.out.println("#######afterPay");
    }

}
