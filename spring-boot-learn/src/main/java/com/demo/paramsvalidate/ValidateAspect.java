package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
        ValidateConfig validateConfig = getConfigs(joinPoint);

        //获取校验级别
        String level = ValidateUtils.isNotBlank(validateConfig.getLevel()) ? validateConfig.getLevel() : validateInterface.getLevel();
        if (!(PvLevel.LOOSE.equals(level) || PvLevel.STRICT.equals(level))){
            ValidateUtils.logWarning("@ParamsValidate校验级别设置错误，将使用默认[PvLevel.STRICT]校验级别");
            level = PvLevel.STRICT;
        }

        ResultValidate resultValidate = new ResultValidate();
        if (PvLevel.STRICT.equals(level)){
            resultValidate = validateMain.validateExecute(joinPoint, validateConfig);  //校验
        }else {
            try {
                resultValidate = validateMain.validateExecute(joinPoint, validateConfig);  //校验
            }catch (Exception e){
                resultValidate.setPass(true);  //PvLevel.LOOSE发生异常不校验

                //打印告警日志
                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                Method method = methodSignature.getMethod();
                String msg = String.format("@ParamsValidate校验发生异常，校验级别为[PvLevel.LOOSE]，不校验。Method:%s.%s", method.getDeclaringClass().getName(),method.getName());
                ValidateUtils.logWarning(msg);
            }
        }
        if (resultValidate.isPass()){  //校验通过
            obj = ((ProceedingJoinPoint) joinPoint).proceed();
        }else {  //校验未通过
            obj = validateInterface.validateNotPass(resultValidate);
        }
        return obj;
    }

    //获取校验注解@ParamsValidate设置的值
    private static ValidateConfig getConfigs(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();  //获取当前方法
        ValidateConfig validateConfig = new ValidateConfig();
        if (method.getAnnotation(ParamsValidate.class) != null){
            String file = method.getAnnotation(ParamsValidate.class).value();
            file = ValidateUtils.isNotBlank(file) ? file : method.getAnnotation(ParamsValidate.class).file();
            String key = method.getAnnotation(ParamsValidate.class).key();
            String level = method.getAnnotation(ParamsValidate.class).level();
            validateConfig.setFile(file);
            validateConfig.setKey(key);
            validateConfig.setLevel(level);
        }
        return validateConfig;
    }

}
