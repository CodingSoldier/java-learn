package com.learn.e_aop.d_cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @author chenpiqian
 * @date: 2020-08-06
 */
public class ProxyUtil {

    public static <T> T createProxy(T target, MethodInterceptor methodInterceptor){
        return (T) Enhancer.create(target.getClass(), methodInterceptor);
    }

}
