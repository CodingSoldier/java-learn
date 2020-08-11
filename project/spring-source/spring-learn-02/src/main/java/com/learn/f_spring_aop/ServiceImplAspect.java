package com.learn.f_spring_aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class ServiceImplAspect {

	@Pointcut("execution(* com.learn.f_spring_aop.a_service.A_ServiceImpl.*(..))")
	public void plugin(){}

	@Before("plugin()")
	public void before(JoinPoint joinpoint){
		System.out.println("#####before###"+joinpoint);
	}

	@After("plugin()")
	public void after(JoinPoint joinpoint){
		System.out.println("#####after###"+joinpoint);
	}

	@AfterReturning(pointcut = "plugin()", returning = "returnValue")
	public void afterReturning(JoinPoint joinPoint, Object returnValue){
		System.out.println("#####afterReturning###"+joinPoint+"，###returnValue###"+returnValue);
	}


}
