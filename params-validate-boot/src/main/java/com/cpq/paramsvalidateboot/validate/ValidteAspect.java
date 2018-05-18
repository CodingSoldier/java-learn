package com.cpq.paramsvalidateboot.validate;


import com.cpq.paramsvalidateboot.validate.bean.AnnoField;
import com.cpq.paramsvalidateboot.validate.bean.ResultCheck;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ValidteAspect {

    @Autowired
    ValidateMain validateMain;
    @Autowired
    ParamsValidateInterface paramsValidateInterface;

    @Pointcut("@annotation(com.cpq.paramsvalidateboot.validate.ParamsValidate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(JoinPoint joinPoint){
        System.out.println("@Around环绕通知，方法执行前");
        Object obj = null;
        try {
            ResultCheck resultCheck = this.validateResult(joinPoint);
            if (resultCheck.getPass() != false){
                obj = ((ProceedingJoinPoint) joinPoint).proceed();
            }else{
                obj = paramsValidateInterface.validateNotPass(resultCheck);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return obj;
    }

    //校验结果
    private ResultCheck validateResult(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Method method = getCurrentMethod(joinPoint);
        AnnoField annoField = getAnnoFields(method);
        Map<String, Object> requestMap = getParamFromRequest(request);
        Object bodyObj = getBodyParam(joinPoint);

        return validateMain.checkHandle(annoField, requestMap, bodyObj);
    }

    //获取当前方法
    private Method getCurrentMethod(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method;
    }

    //获取校验注解@ParamsValidate设置的值
    private AnnoField getAnnoFields(Method method){
        AnnoField annoField = new AnnoField();
        if (method.getAnnotation(ParamsValidate.class) != null){
            String file = method.getAnnotation(ParamsValidate.class).file();
            String keyName = method.getAnnotation(ParamsValidate.class).keyName();
            annoField.setFile(file);
            annoField.setKeyName(keyName);
        }
        return annoField;
    }

    //获取@RequestBody的参数
    private Object getBodyParam(JoinPoint joinPoint){
        Object result = null;
        Method method = getCurrentMethod(joinPoint);
        Annotation[][] arr2 = method.getParameterAnnotations();
        if (arr2 != null){
            outFor:for (int oi=0; oi < arr2.length; oi++){
                if (arr2[oi] != null){
                    for (int ii=0; ii < arr2[oi].length; ii++){
                        if ("RequestBody".equals(arr2[oi][ii].annotationType().getSimpleName())){
                            result = joinPoint.getArgs()[oi];
                            break outFor;
                        }
                    }
                }
            }
        }
        return result;
    }

    //从request中获取请求参数
    private Map<String, Object> getParamFromRequest(HttpServletRequest request){
        if (request == null){
            return new HashMap<String, Object>();
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] value = null;
        String temp = "";
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null){
            for (String key:paramMap.keySet()){
                if (Util.isNotBlank(key)){
                    value = paramMap.get(key);
                    if (value == null){
                        resultMap.put(key, value);
                    }else{
                        temp = "";
                        for (String elem:value){
                            temp += elem + ",";
                        }
                        temp = value.length > 0 ? temp.substring(0, temp.length() -1) : temp;
                        resultMap.put(key, temp);
                    }
                }
            }
        }

        return resultMap;
    }

}
