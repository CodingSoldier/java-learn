package com.spring.aoplog;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    //使用了@Log注解的方法是一个切入点Pointcut
    @Pointcut("@annotation(com.spring.aoplog.Log)")
    public void aspect(){}

    @Before("aspect()")
    public void before(JoinPoint joinPoint){
        String method = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()";
        System.out.println("@Before前置通知，进入方法："+method);
    }

}
