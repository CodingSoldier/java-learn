package com.example.cpq.dspringmvcauto.servlet.support;

import com.example.cpq.dspringmvcauto.config.DispatcherServletConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * spring mvc 自动装配的默认实现
 * 父类的org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)会在服务启动时被调用
 */
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
     * 加载DispatcherServletConfiguration
     * DispatcherServletConfiguration有@ComponentScan("com.example.cpq.dspringmvcauto")
     * @ComponentScan("com.example.cpq.dspringmvcauto")扫描WebMvcConfig
     * WebMvcConfig配置了mapping、adapter
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DispatcherServletConfig.class};
    }

}
