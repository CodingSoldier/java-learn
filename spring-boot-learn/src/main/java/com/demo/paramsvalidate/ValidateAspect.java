package com.demo.paramsvalidate;


import com.demo.paramsvalidate.bean.ValidateConfig;
import com.demo.paramsvalidate.bean.ResultValidate;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Aspect
@Component
public class ValidateAspect {

    @Autowired
    ValidateMain validateMain;
    @Autowired
    ValidateInterface validateInterface;

    @Pointcut("@annotation(com.demo.paramsvalidate.ParamsValidate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(JoinPoint joinPoint){
        Object obj = null;
        ResultValidate resultValidate = new ResultValidate(true);
        try {
            resultValidate = this.validateResult(joinPoint);
            obj = resultValidate.isPass() != false ? ((ProceedingJoinPoint) joinPoint).proceed()
                    : validateInterface.validateNotPass(resultValidate);
        }catch (Throwable e){
            resultValidate.setPass(false);
            resultValidate.setMsgSet(new HashSet(){{add("服务暂不可用");}});
            obj = validateInterface.validateNotPass(resultValidate);
            e.printStackTrace();
        }
        return obj;
    }

    //校验结果
    private ResultValidate validateResult(JoinPoint joinPoint){
        ResultValidate resultValidate = new ResultValidate(true);
        try {
            Method method = getCurrentMethod(joinPoint);
            ValidateConfig validateConfig = getConfigs(method);
            if (Utils.isNotBlank(validateConfig.getFile())){
                Map<String, Object> allParam = mergeParams(joinPoint);
                resultValidate = validateMain.validateHandle(validateConfig, allParam);
            }
        }catch (IOException e){
            resultValidate.setPass(false);
            resultValidate.setMsgSet(new HashSet<String>(){{
                add("@ParamsValidate无法处理请求参数");
            }});
            e.printStackTrace();
        }
        return resultValidate;
    }

    //获取当前方法
    private Method getCurrentMethod(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method;
    }

    //获取校验注解@ParamsValidate设置的值
    private ValidateConfig getConfigs(Method method){
        ValidateConfig validateConfig = new ValidateConfig();
        if (method.getAnnotation(ParamsValidate.class) != null){
            String file = method.getAnnotation(ParamsValidate.class).file();
            String keyName = method.getAnnotation(ParamsValidate.class).keyName();
            validateConfig.setFile(file);
            validateConfig.setKeyName(keyName);
        }
        return validateConfig;
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
                if (Utils.isNotBlank(key)){
                    value = paramMap.get(key);
                    if (value.length == 1){
                        resultMap.put(key, value[0]);
                    }else{
                        resultMap.put(key, Arrays.asList(value));
                    }
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
