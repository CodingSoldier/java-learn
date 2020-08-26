package self.licw.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.core.BeanContainer;
import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.annotation.RequestMapping;
import self.licw.simpleframework.mvc.annotation.RequestParam;
import self.licw.simpleframework.mvc.annotation.ResponseBody;
import self.licw.simpleframework.mvc.processor.RequestProcessor;
import self.licw.simpleframework.mvc.render.ResultRender;
import self.licw.simpleframework.mvc.render.impl.JsonRender;
import self.licw.simpleframework.mvc.render.impl.ResourceNotFoundResultRender;
import self.licw.simpleframework.mvc.render.impl.ViewResultRender;
import self.licw.simpleframework.mvc.type.ControllerMethod;
import self.licw.simpleframework.mvc.type.RequestPathInfo;
import self.licw.simpleframework.util.ConverterUtil;
import self.licw.simpleframework.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * controller请求处理器
 * 功能：
 * 1、针对特定请求，选择匹配的Controller方法进行处理
 * 2、解析请求里的参数及其对应的值，并赋值给Controller方法的参数
 * 3、选择合适的Render，为后续请求处理结果的渲染做准备
 */
@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {
    // IOC容器
    private BeanContainer beanContainer;
    // 客户端请求和Controller方法的映射集合
    private Map<RequestPathInfo, ControllerMethod> pathAndControllerMethodMap = new ConcurrentHashMap<>();

    /**
     * 依靠容器的能力，建立请求路径，请求方法与Controller方法实例的映射
     */
    public ControllerRequestProcessor(){
        this.beanContainer = BeanContainer.getInstance();
        // 获取所有被@RequestMapping标识的类
        Set<Class<?>> requestMappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        initPathControllerMethodMap(requestMappingSet);
    }

    /**
     * 将requestMapping的ur、请求方法 与 Controller做绑定
     */
    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if (ValidationUtil.isEmpty(requestMappingSet)){
            return;
        }
        // 1、遍历所有被@RequestMapping标记的类
        for (Class<?> requestMappingClass : requestMappingSet) {
            // 2、获取类上边该注解的属性值作为一级路径
            RequestMapping requestMapping = requestMappingClass.getAnnotation(RequestMapping.class);
            String basePath = requestMapping.value();
            basePath = !basePath.startsWith("/") ? "/" + basePath : basePath;
            // 3、遍历类里面所有被@RequestMapping标记的方法，获取方法上面的注解属性值作为二级路径
            Method[] methods = requestMappingClass.getDeclaredMethods();
            if (ValidationUtil.isEmpty(methods)){
                continue;
            }
            for (Method method : methods) {
                // 判断改方法是否被@RequestMapping标注
                if (method.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping methodRequest = method.getAnnotation(RequestMapping.class);
                    String methodPath = methodRequest.value();
                    methodPath = !methodPath.startsWith("/") ? "/" + methodPath : methodPath;
                    String url = basePath + methodPath;
                    /**
                     * 4、解析改方法里被@RequestParam标记的参数
                     * 获取该注解的属性值，参数名作为key，参数类型作为value
                     */
                    HashMap<String, Class<?>> methodParams = new HashMap<>(16);
                    Parameter[] parameters = method.getParameters();
                    if (!ValidationUtil.isEmpty(parameters)){
                        for (Parameter parameter : parameters) {
                            RequestParam param = parameter.getAnnotation(RequestParam.class);
                            if (param == null){
                                throw new RuntimeException("参数都需要使用@RequestParam");
                            }
                            methodParams.put(param.value(), parameter.getType());
                        }
                    }
                    // 5、将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例，放到映射表里
                    String httpMethod = String.valueOf(methodRequest.method());
                    RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod, url);
                    // 如果之前存放了改key（匹配路径和方法写了多次），则直接覆盖
                    if (this.pathAndControllerMethodMap.containsKey(requestPathInfo)){
                        log.warn("url重复：", requestPathInfo.getHttpPath());
                    }
                    ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass, method, methodParams);
                    this.pathAndControllerMethodMap.put(requestPathInfo, controllerMethod);
                }
            }
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 1、解析HttpServletRequest的请求方法、请求路径。获取对应的ControllerMethod实例。
        String method = requestProcessorChain.getRequestMethod();
        String path = requestProcessorChain.getRequestPath();
        RequestPathInfo requestPathInfo = new RequestPathInfo(method, path);
        ControllerMethod controllerMethod = this.pathAndControllerMethodMap.get(requestPathInfo);
        if (controllerMethod == null){
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(path, method));
            return false;
        }
        // 2、解析请求参数，并传递参数给获取到的ControllerMethod实例去执行。
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        // 3、根据结果，选择render渲染。
        setResultRender(result, controllerMethod, requestProcessorChain);
        return true;
    }

    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain){
        // 使用默认的渲染器
        if (result == null){
            return;
        }
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        ResultRender resultRender = isJson ? new JsonRender(result) : new ViewResultRender(result);
        requestProcessorChain.setResultRender(resultRender);
    }

    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request){
        // 1、从请求中获取GET或者POST的参数名及其对应的值，因为request.getParameterMap只能获取GET/POST请求过来的所有参数
        HashMap<String, String> requestParamMap = new HashMap<>();
        // 获取GET/POST请求的所有参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            String[] value = parameterMap.get(key);
            if (!ValidationUtil.isEmpty(value)){
                requestParamMap.put(key, value[0]);
            }
        }
        // 2、根据上边获取到的请求参数名和对应的值，以及之前ControllerMethod的参数名与类型的映射关系，实例化方法对应的参数
        ArrayList<Object> methodParams = new ArrayList<>();
        Map<String, Class<?>> methodParamMap = controllerMethod.getMethodParameters();
        for (String key : methodParamMap.keySet()) {
            Class<?> type = methodParamMap.get(key);
            String valueStr = requestParamMap.get(key);
            Object value =  valueStr == null ? ConverterUtil.primitveNull(type) : ConverterUtil.convert(type,valueStr);
            methodParams.add(value);
        }

        // 3、使用反射执行controller方法
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result;
        try {
            result = methodParams.size() == 0 ? invokeMethod.invoke(controller) : invokeMethod.invoke(controller, methodParams.toArray());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }
}
