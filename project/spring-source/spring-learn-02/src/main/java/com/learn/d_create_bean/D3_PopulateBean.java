package com.learn.d_create_bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.learn.d_create_bean")
public class D3_PopulateBean {

	/**
	 populateBean()方法
	 	调用Bean的Setter方法实例去给Bean设置上属性值
	 	变量类型的转换，同时hi啊哟啊考虑处理集合类型的情况
	 	处理显示自动装配的逻辑（Autowired byName/byType）

	 AbstractAutowireCapableBeanFactory#populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw)
	 断点调试
	 */
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(D3_PopulateBean.class);

	}

}
