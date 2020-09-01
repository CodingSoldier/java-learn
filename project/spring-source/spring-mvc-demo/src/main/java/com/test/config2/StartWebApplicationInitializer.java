//package com.test.config2;
//
//import org.springframework.web.filter.CharacterEncodingFilter;
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//import javax.servlet.Filter;
//
///**
// * @author chenpiqian
// * @date: 2020-08-27
// */
//public class StartWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//	/**
//	 * SpringContext中相关的bean
//	 */
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		return new Class<?>[]{SpringRootConfig.class};
//	}
//
//	/**
//	 * DispatcherServlet中的bean
//	 */
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return new Class<?>[]{MvcConfig.class};
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
//	 * 拦截并处理请求编码
//	 * @return
//	 */
//	@Override
//	protected Filter[] getServletFilters() {
//		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
//		encodingFilter.setEncoding("UTF-8");
//		encodingFilter.setForceEncoding(true);
//		return new Filter[]{encodingFilter};
//	}
//}
//
//
//
//
