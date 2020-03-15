package com.example.ee3rest.controller3;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class RestWebMvcConfigurer implements WebMvcConfigurer {

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
