package com.learn.e_aop.c_jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxyUtil {
	public static <T>T newProxyInstance(T targetObject, InvocationHandler handler){
		ClassLoader classLoader = targetObject.getClass().getClassLoader();
		Class<?>[]  interfaces = targetObject.getClass().getInterfaces();
		return (T)Proxy.newProxyInstance(classLoader, interfaces, handler);
	}
}
