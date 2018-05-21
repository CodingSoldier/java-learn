package com.cpq.paramsvalidateboot.validate;


import com.cpq.paramsvalidateboot.validate.bean.AnnotationField;
import com.cpq.paramsvalidateboot.validate.bean.ResultCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Aspect
@Component
public class ValidateAspect {

    @Autowired
    ValidateMain validateMain;
    @Autowired
    ParamsValidateInterface paramsValidateInterface;

    @Pointcut("@annotation(com.cpq.paramsvalidateboot.validate.ParamsValidate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(JoinPoint joinPoint){
        Object obj = null;
        try {
            ResultCheck resultCheck = this.validateResult(joinPoint);
            obj = resultCheck.isPass() != false ? ((ProceedingJoinPoint) joinPoint).proceed()
                    : paramsValidateInterface.validateNotPass(resultCheck);
        }catch (Throwable e){
            e.printStackTrace();
        }
        return obj;
    }

    //校验结果
    private ResultCheck validateResult(JoinPoint joinPoint){
        ResultCheck resultCheck = new ResultCheck(true);
        try {
            Method method = getCurrentMethod(joinPoint);
            AnnotationField annoField = getAnnoFields(method);
            if (Util.isNotBlankObj(annoField.getFile())){
                Map<String, Object> allParam = mergeParams(joinPoint);
                resultCheck = validateMain.checkHandle(annoField, allParam);
            }
        }catch (IOException e){
            resultCheck.setPass(false);
            resultCheck.setMsgSet(new HashSet<String>(){{
                add("@ParamsValidate无法处理请求参数");
            }});
            e.printStackTrace();
        }
        return resultCheck;
    }

    //获取当前方法
    private Method getCurrentMethod(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method;
    }

    //获取校验注解@ParamsValidate设置的值
    private AnnotationField getAnnoFields(Method method){
        AnnotationField annoField = new AnnotationField();
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
        if (request == null)
            return new HashMap<>();

        Map<String, Object> resultMap = new HashMap<>();
        String[] value = null;
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null){
            for (String key:paramMap.keySet()){
                if (Util.isNotBlankObj(key)){
                    value = paramMap.get(key);
                    resultMap.put(key, value);
                }
            }
        }
        return resultMap;
    }

    //合并请求参数
    private Map<String, Object> mergeParams(JoinPoint joinPoint) throws IOException{
        Object body = getBodyParam(joinPoint);
        Map<String, Object> bodyMap = objToMap(body);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Object> paramMap = getParamFromRequest(request);

        for (String key:bodyMap.keySet()){
            paramMap.put(key, bodyMap.get(key));
        }
        return paramMap;
    }

    //对象变成map
    private Map<String, Object> objToMap(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        Map<String, Object> result = mapper.readValue(json, Map.class);
        return result != null ? result : new HashMap<>();
    }

}
