//package com.test.config;
//
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
///**
// * @author chenpiqian
// * @date: 2020-08-27
// */
//public class StartWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//	/**
//	 * 替代了web.xml
//	 */
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		return new Class<?>[0];
//	}
//
//	/**
//	 * web.xml中的servlet-mapping
//	 */
//	@Override
//	protected String[] getServletMappings() {
//		return new String[]{"/"};
//	}
//
//	/**
//	 * 相当于web.xml中配置servlet为DispatcherServlet
//	 * 加载DispatcherServletConfiguration
//	 * DispatcherServletConfiguration有@ComponentScan("com.example.cpq.dspringmvcauto")
//	 * @ComponentScan("com.example.cpq.dspringmvcauto")扫描WebMvcConfig
//	 * WebMvcConfig配置了mapping、adapter
//	 */
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return new Class<?>[]{DispatcherServletConfig.class};
//	}
//}
