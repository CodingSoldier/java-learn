package com.learn.e_aop.d_cglib;

/**
 * @author chenpiqian
 * @date: 2020-08-06
 */
public class RunCglib {

    public static void main(String[] args) {
        AspectMethodInterceptor aspectMethodInterceptor = new AspectMethodInterceptor();
        BasePay basePay = new BasePay();

        BasePay proxyBasePay = ProxyUtil.createProxy(basePay, aspectMethodInterceptor);
        proxyBasePay.pay();

    }

}
