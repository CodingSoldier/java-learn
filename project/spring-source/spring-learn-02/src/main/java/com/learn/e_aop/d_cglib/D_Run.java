package com.learn.e_aop.d_cglib;

import org.springframework.cglib.proxy.MethodInterceptor;

public class D_Run {

    /**
     CGLIB动态代理
        不要求被代理类实现接口
        内部主要封装了ASM Java字节码操控框架
        动态生成子类以覆盖非final的方法，绑定钩子回调自定义拦截器
     */

    public static void main(String[] args) {
        CommonPayment commonPayment = new CommonPayment();
        MethodInterceptor methodInterceptor = new AlipayMethodInterceptor();
        CommonPayment proxy = CgliUtil.createProxy(commonPayment, methodInterceptor);
        proxy.pay();
    }

}
