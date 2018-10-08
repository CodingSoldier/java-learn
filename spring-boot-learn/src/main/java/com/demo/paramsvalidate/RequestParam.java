package com.demo.paramsvalidate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestParam {

    //合并请求参数
    public Map<String, Object> mergeParams(JoinPoint joinPoint) throws IOException{
        Object body = getBodyParam(joinPoint);
        Map<String, Object> bodyMap = bodyParamToMap(body);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Object> paramMap = getParamFromRequest(request);

        for (Map.Entry<String, Object> entry:bodyMap.entrySet()){
            paramMap.put(entry.getKey(), entry.getValue());
        }
        return paramMap;
    }

    //获取@RequestBody的参数
    private Object getBodyParam(JoinPoint joinPoint){
        Object result = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();  //获取当前方法
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

    //body中的参数添加到map
    private Map<String, Object> bodyParamToMap(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        Map<String, Object> result = mapper.readValue(json, Map.class);
        return result != null ? result : new HashMap<>();
    }

    //从request中获取请求参数(不包含body)
    private Map<String, Object> getParamFromRequest(HttpServletRequest request){
        if (request == null)
            return new HashMap<>();

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null){
            for (Map.Entry<String, String[]> entry:paramMap.entrySet()){
                if (ValidateUtils.isNotBlank(entry.getKey())){
                    if (entry.getValue().length == 1){
                        resultMap.put(entry.getKey(), entry.getValue()[0]);
                    }else{
                        resultMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
                    }
                }
            }
        }
        return resultMap;
    }

}
