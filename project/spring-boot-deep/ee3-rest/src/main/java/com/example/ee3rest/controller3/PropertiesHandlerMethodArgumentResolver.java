package com.example.ee3rest.controller3;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
自定义参数解析器
 可以去掉@RequestBody
 */
public class PropertiesHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 此解析器是否能解析方法参数
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Properties.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
        HttpServletRequest request = servletWebRequest.getRequest();
        HttpInputMessage httpInputMessage = new ServletServerHttpRequest(request);

        // converter.read()返回的是Properties对象
        // 本方法实际上跟PropertiesHttpMessageConverter.readInternal()做的事情一样，也是要将requestBody转为Properties对象
        PropertiesHttpMessageConverter converter = new PropertiesHttpMessageConverter();
        return converter.read(null, null, httpInputMessage);
    }
}
