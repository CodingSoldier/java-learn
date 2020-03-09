package com.example.cpq.espringboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @EnableWebMvc必须注释掉
 * 因为WebMvcAutoConfiguration有装配条件@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
 * WebMvcAutoConfiguration类是完全自动装配
 * @EnableWebMvc是手动装配
 */
//@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {
                System.out.println("##########添加前置过滤器");
                return true;
            }
        });
    }
}
