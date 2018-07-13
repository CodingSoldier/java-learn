package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class ValidateAspect {

    private static final Logger LOGGER = Logger.getLogger("@ParamsValidate");

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
        ResultValidate resultValidate = this.validateResult(joinPoint);
        if (resultValidate.isPass()){  //校验通过
            obj = ((ProceedingJoinPoint) joinPoint).proceed();
        }else {  //校验未通过
            obj = validateInterface.validateNotPass(resultValidate);
        }
        return obj;
    }

    //校验结果
    private ResultValidate validateResult(JoinPoint joinPoint){
        //默认是校验通过
        ResultValidate resultValidate = new ResultValidate(true);
        Method method = getCurrentMethod(joinPoint);
        ValidateConfig validateConfig = getConfigs(method);

        //需要校验
        if (ValidateUtils.isNotBlank(validateConfig.getFile())){
            Map<String, Object> allParam = null;
            try {
                allParam = mergeParams(joinPoint);
            }catch (IOException e){
                //异常，无法处理请求参数，返回pass false
                resultValidate.setPass(false);
                resultValidate.setMsgSet(new HashSet<String>(){{
                    add("@ParamsValidate无法处理请求参数");
                }});
                LOGGER.log(Level.SEVERE, "@ParamsValidate无法处理请求参数", e);
            }

           //正常获取请求参数，可校验
           if (allParam != null)
                resultValidate = validateMain.validateEntry(validateConfig, allParam);

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
            String file = method.getAnnotation(ParamsValidate.class).value();
            file = ValidateUtils.isNotBlank(file) ? file : method.getAnnotation(ParamsValidate.class).file();
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
                if (ValidateUtils.isNotBlank(key)){
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
        Map<String, Object> bodyMap = bodyParamToMap(body);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Object> paramMap = getParamFromRequest(request);

        for (String key:bodyMap.keySet()){
            paramMap.put(key, bodyMap.get(key));
        }
        return paramMap;
    }

    //body中的参数添加到map
    private Map<String, Object> bodyParamToMap(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        Map<String, Object> result = mapper.readValue(json, Map.class);
        return result != null ? result : new HashMap<>();
    }

}
