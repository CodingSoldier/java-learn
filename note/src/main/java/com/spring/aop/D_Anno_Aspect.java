package com.spring.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class D_Anno_Aspect {

    @Pointcut("@annotation(com.spring.aop.DAnnoLog)")
    public void aspect(){}

    @Before("aspect()")
    public void before(JoinPoint joinPoint){
        System.out.println("@Before前置通知");
    }

    @Around("aspect()")
    public Object around(JoinPoint joinPoint){
        //System.out.println("@Around环绕通知，方法执行前");
        Object obj = null;
        try {
            obj = ((ProceedingJoinPoint) joinPoint).proceed();

            //改变返回值切入方法的返回值
            System.out.println(obj.getClass().getName());
            if (obj instanceof Map){
                Map<String, Object> map = (Map<String, Object>)obj;
                map.put("key2", "val2");
                return map;
            }

        }catch (Throwable e){
            System.out.println("@Around执行方法异常");
        }
        //System.out.println("@Around环绕通知，方法执行后");
        return obj;
    }

    @After("aspect()")
    public void after(JoinPoint joinPoint){
        //System.out.println("@After后置通知");
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        String operationType = "";
        String operationName = "";

        try{
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for(Method method:methods){
                if(StringUtils.equals(methodName, method.getName())){
                    Class[] clazz = method.getParameterTypes();
                    if(clazz.length == arguments.length){
                        operationType = method.getAnnotation(DAnnoLog.class).operationType();
                        operationName = method.getAnnotation(DAnnoLog.class).operationName();
                        //break;
                    }
                }
            }
            System.out.println(targetName);
            System.out.println(methodName);
            System.out.println(arguments);
            System.out.println(operationType);
            System.out.println(operationName);
        }catch (Exception e){
            System.out.println("@After后置通知异常");
        }
    }

    @AfterReturning("aspect()")
    public void afterReturning(JoinPoint joinPoint){
        //System.out.println("@AfterReturning后置返回通知。"+joinPoint.toString());
    }

    @AfterThrowing(pointcut="aspect()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        //System.out.println("@AfterThrowing抛出异常后通知。"+joinPoint.toString());
        ex.printStackTrace();
    }

}
