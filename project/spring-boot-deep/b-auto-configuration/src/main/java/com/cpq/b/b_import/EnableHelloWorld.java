package com.cpq.b.b_import;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
/**
 * 使用模块装配类，HelloWorldConfiguration.class
 */
@Import(HelloWorldConfiguration.class)
public @interface EnableHelloWorld {
}
