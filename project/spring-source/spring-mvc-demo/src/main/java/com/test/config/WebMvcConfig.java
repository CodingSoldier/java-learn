package com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
@Configuration 配置类

@EnableWebMvc 激活SpringMVC，自动装配HandlerMapping、HandlerAdapter。
    org.springframework.web.servlet.DispatcherServlet#getHandler()
        this.handlerMappings装配了5个类
    org.springframework.web.servlet.DispatcherServlet#getHandlerAdapter()
        this.handlerAdapters装配了3个类
    相当于替换了app-context.xml中的：
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>

 @EnableWebMvc
    @Import(DelegatingWebMvcConfiguration.class) 注入DelegatingWebMvcConfiguration.class
    DelegatingWebMvcConfiguration 继承 WebMvcConfigurationSupport
    WebMvcConfigurationSupport配置5个HandlerMapping、3个HandlerAdapter

    org.springframework.web.servlet.DispatcherServlet#initHandlerMappings()
    org.springframework.web.servlet.DispatcherServlet#initHandlerAdapters()
    使用bean工厂加载HandlerMapping、HandlerAdapter

 实现WebMvcConfigurer，配置spring mvc自定义组件
 DelegatingWebMvcConfiguration#setConfigurers(List<WebMvcConfigurer> configurers)会将WebMvcConfigurer实现类添加到DelegatingWebMvcConfiguration#configurers中

 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 使用@Bean配置ViewResolver，替换app-context.xml中的配置
     * org.springframework.web.servlet.DispatcherServlet#resolveViewName()
     *      this.viewResolvers()获取到两个ViewResolvers，第二个是本bean
     */
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * 添加前置过滤器
     * 在
     org.springframework.web.servlet.DispatcherServlet#getHandler()
     org.springframework.web.servlet.DispatcherServlet#getHandlerAdapter()
     之后执行
     */
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
