package com.example.cpq.dspringmvcauto;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DefaultAnnotationConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    //替代了web.xml
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    /**
     * 相当于web.xml中配置servlet
     * 加载DispatcherServletConfiguration
     * DispatcherServletConfiguration有@ComponentScan("com.example.cpq.dspringmvcauto")
     * @ComponentScan("com.example.cpq.dspringmvcauto")扫描WebMvcConfig
     * WebMvcConfig配置了mapping、adapter
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DispatcherServletConfiguration.class};
    }

    /**
     * web.xml中的servlet-mapping的/
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
