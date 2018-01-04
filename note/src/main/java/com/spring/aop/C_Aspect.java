package com.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class C_Aspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(C_Aspect.class);

    @Pointcut("execution (* com.spring.aop.c.service..*.*(..))")
    private void aspect(){
        System.out.println("C_Aspect.aspect()方法执行");
    }

    @Around("aspect()")
    public void around(JoinPoint joinPoint){
        System.out.println("@Around环绕通知。执行方法前" + joinPoint.toString());
        try {
            ((ProceedingJoinPoint)joinPoint).proceed();
        }catch (Throwable e){
            System.out.println("@Around中调用对象方法");
        }

        System.out.println("@Around环绕通知。方法执行后" + joinPoint.toString());
    }

    @Before("aspect()")
    public void before(JoinPoint joinPoint){
        System.out.println("@Before前置通知。" + joinPoint.toString());
    }

    @After("aspect()")
    public void after(JoinPoint joinPoint){
        System.out.println("@After后置通知。"+joinPoint.toString());
    }

    @AfterReturning("aspect()")
    public void afterReturning(JoinPoint joinPoint){
        System.out.println("@AfterReturning后置返回通知。"+joinPoint.toString());
    }

    @AfterThrowing(pointcut="aspect()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        System.out.println("@AfterThrowing抛出异常后通知。"+joinPoint.toString());
        ex.printStackTrace();
    }

}
