package com.example.ee4springservlet.servlet.support;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * spring mvc 自动装配的默认实现
 * 父类的org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)会在服务启动时被调用
 */
@ComponentScan("com.example.ee4springservlet.controller")
public class DefaultAnnotationConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * 替代了web.xml
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    /**
     * web.xml中的servlet-mapping
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 相当于web.xml中配置servlet为DispatcherServlet
     * 加载当前类，当前类有@ComponentScan
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{getClass()};
    }

}
