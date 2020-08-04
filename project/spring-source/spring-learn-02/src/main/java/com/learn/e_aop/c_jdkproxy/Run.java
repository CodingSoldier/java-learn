package com.learn.e_aop.c_jdkproxy;

public class Run {

	public static void main(String[] args) {
		ToBPay toBPay = new ToBPayImpl();
		AlipayInvocationHandler aliHandler = new AlipayInvocationHandler(toBPay);
		ToBPay toBPayProxy = JdkProxyUtil.newProxyInstance(toBPay, aliHandler);
		toBPayProxy.pay();

		ToCPay toCPay = new ToCPayImpl();
		AlipayInvocationHandler aliCHandler = new AlipayInvocationHandler(toCPay);
		ToCPay toCPayProxy = JdkProxyUtil.newProxyInstance(toCPay, aliCHandler);
		toCPayProxy.pay();


	}

}
