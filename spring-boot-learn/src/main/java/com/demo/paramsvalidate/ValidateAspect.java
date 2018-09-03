package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.ResultValidate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * author chenpiqian 2018-05-25
 */
@Aspect
@Component
public class ValidateAspect {

    public static final String VALIDATE_EXCEPTION_MSG ="服务暂不可用";

    @Autowired
    ValidateMain validateMain;
    @Autowired
    ValidateInterface validateInterface;

    @Pointcut("@annotation(com.demo.paramsvalidate.ParamsValidate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(JoinPoint joinPoint) throws Throwable{
        Object obj = null;
        ResultValidate resultValidate = validateMain.validateResult(joinPoint);
        if (resultValidate.isPass()){  //校验通过
            obj = ((ProceedingJoinPoint) joinPoint).proceed();
        }else {  //校验未通过
            obj = validateInterface.validateNotPass(resultValidate);
        }
        return obj;
    }

}
