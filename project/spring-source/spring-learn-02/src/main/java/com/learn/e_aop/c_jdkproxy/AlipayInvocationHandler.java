package com.learn.e_aop.c_jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AlipayInvocationHandler implements InvocationHandler {

	private Object targetObject;

	public AlipayInvocationHandler(Object targetObject) {
		this.targetObject = targetObject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		beforePay();
		Object result = method.invoke(targetObject, args);
		afterPay();
		return result;
	}

	private void beforePay(){
		System.out.println("#####beforePay");
	}

	private void afterPay(){
		System.out.println("#####afterPay");
	}


}
