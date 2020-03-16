package com.example.ee3rest.controller3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 由于实现addArgumentResolvers()方法无法将自定义ArgumentResolver的顺序提前
     * 所以在RequestMappingHandlerAdapter实例化后，再手动修改ArgumentResolver顺序
     *
     * HandlerMethodReturnValueHandler也是一样，初始化完之后再改顺序
     * WebMvcConfigurer#addReturnValueHandlers(java.util.List)
     */
    @PostConstruct
    public void init(){
        List<HandlerMethodArgumentResolver> resolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        ArrayList<HandlerMethodArgumentResolver> newResolvers = new ArrayList<>(resolvers.size() + 1);
        newResolvers.add(new PropertiesHandlerMethodArgumentResolver());
        newResolvers.addAll(resolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(newResolvers);

        List<HandlerMethodReturnValueHandler> handlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(handlers.size() + 1);
        newHandlers.add(new PropertiesHandlerMethodReturnValueHandler());
        newHandlers.addAll(handlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }


///**
    // * 添加自定义参数解析器
    // * 由于自定义的参数解析器会被spring添加到内置解析器之后，所以这种方式行不通
    // */
    //@Override
    //public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    //    if (resolvers.isEmpty()){
    //        resolvers.add(new PropertiesHandlerMethodArgumentResolver());
    //    }else {
    //        resolvers.set(0, new PropertiesHandlerMethodArgumentResolver());
    //    }
    //}

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /**
         * 由于ControllerConverter.addProps使用@responseBody，所以返回值会由RequestResponseBodyMethodProcessor处理
         * RequestResponseBodyMethodProcessor的messageConverters有11个，必须将PropertiesHttpMessageConverter的顺序提升到MappingJackson2HttpMessageConverter之前，不然返回值序列化会被MappingJackson2HttpMessageConverter处理
         */
        //converters.add(0, new PropertiesHttpMessageConverter());

        /**
         * 如果将PropertiesHttpMessageConverter添加到converter最后
         * 序列化返回值的时候想用PropertiesHttpMessageConverter#writeInternal()就必须在controller方法中添加produces = "text/properties"
         */
        converters.add(new PropertiesHttpMessageConverter());
    }
}
