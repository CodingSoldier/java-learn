package com.cpq.b.e_auto;

import com.cpq.b.b_import.EnableMyBeans;
import com.cpq.b.d_condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义装配，将多个注解合成一个HelloWorldAutoConfiguration
 * HelloWorldAutoConfiguration添加到工厂装配（META-INF/spring.factories）中
 * EnableAutoConfigurationBootstrap启动自动装配@EnableAutoConfiguration
 */
@Configuration
@EnableMyBeans
@ConditionalOnSystemProperty(name = "user.name", value = "Administrator")
public class HelloWorldAutoConfiguration {
}
