package com.example.ee3rest.controller3;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
自定义返回值处理器，不需要使用@ResponseBody
 */
public class PropertiesHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    // 是否能处理返回值
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Properties.class.equals(returnType.getMethod().getReturnType());
    }

    // 处理返回值
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        HttpServletRequest request = servletWebRequest.getRequest();
        String contentType = request.getHeader("Content-Type");
        MediaType mediaType = MediaType.parseMediaType(contentType);

        HttpServletResponse response = servletWebRequest.getResponse();
        HttpOutputMessage message = new ServletServerHttpResponse(response);

        PropertiesHttpMessageConverter converter = new PropertiesHttpMessageConverter();
        Properties properties = (Properties)returnValue;
        // 通过PropertiesHttpMessageConverter输出响应
        converter.write(properties, mediaType, message);

        // 告知spring mvc 请求已经处理完毕
        mavContainer.setRequestHandled(true);
    }
}
