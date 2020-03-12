package com.example.ee2view.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author chenpiqian
 * @date: 2020-03-10
 */
@Configuration
public class WebMvcConfig {

    /**
     * 框架中配置bean的方式
     * WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#defaultViewResolver()
     * <p>
     * 自定义配置bean
     * 设置InternalResourceViewResolver的优先级高于ThymeleafViewResolver
     */
    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        // ThymeleafViewResolver Ordered.LOWEST_PRECEDENCE - 5
        // 优先级高于 ThymeleafViewResolver
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        // 配置 ViewResolver 的 Content-Type
        resolver.setContentType("text/xml;charset=UTF-8");
        return resolver;
    }

}
