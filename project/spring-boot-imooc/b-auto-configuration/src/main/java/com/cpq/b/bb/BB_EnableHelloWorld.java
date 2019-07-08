package com.cpq.b.bb;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
/**
 * 使用接口方式装配类，HelloWorldImportSelector.class
 */
@Import(HelloWorldImportSelector.class)
public @interface BB_EnableHelloWorld {
}
