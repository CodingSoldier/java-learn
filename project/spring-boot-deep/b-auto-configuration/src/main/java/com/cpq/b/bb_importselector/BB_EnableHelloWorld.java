package com.cpq.b.bb_importselector;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
/**
 * 使用接口方式装配类，HelloWorldImportSelector.class
 *
 * 先加载HelloWorldImportSelector
 * HelloWorldImportSelector 加载 BB_HelloWorldConfiguration
 * BB_HelloWorldConfiguration 通过@Bean创建bean
 *
 * @Import ImportSelector 实现类可以在 HelloWorldImportSelector#selectImports() 方法中添加判断条件
 * 直接@Import(一个配置类就没办法加判断)
 */
@Import(HelloWorldImportSelector.class)
public @interface BB_EnableHelloWorld {
}
