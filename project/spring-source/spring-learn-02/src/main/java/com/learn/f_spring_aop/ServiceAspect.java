package com.learn.f_spring_aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(2)
@Component
public class ServiceAspect {

	@Pointcut("execution(* com.learn.f_spring_aop.a_service..*.*(..))")
	public void plugin(){}

	@Before("plugin()")
	public void before(JoinPoint joinpoint){
		System.out.println("-----ServiceAspect.before---"+joinpoint);
	}

	@After("plugin()")
	public void after(JoinPoint joinpoint){
		System.out.println("-----ServiceAspect.after---"+joinpoint);
	}

	@Around("plugin()")
	public Object around(JoinPoint joinpoint){
		System.out.println("-----ServiceAspect.around--start---"+joinpoint);
		Object returnValue = null;
		try {
			returnValue = ((ProceedingJoinPoint) joinpoint).proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		System.out.println("-----ServiceAspect.around--end---"+joinpoint);
		return returnValue;
	}


}
