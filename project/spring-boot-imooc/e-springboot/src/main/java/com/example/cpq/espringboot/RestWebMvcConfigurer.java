package com.example.cpq.espringboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-12
 */
@Configuration
public class RestWebMvcConfigurer implements WebMvcConfigurer {

    public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        /**
         * converters有多个，必须将自定义的MessageConverter添加到第一位，这样才能用到自定义的converter,原有的converters会往后排
         */
        converters.add(0, new PropertiesHttpMessageConverter());
    }

}
