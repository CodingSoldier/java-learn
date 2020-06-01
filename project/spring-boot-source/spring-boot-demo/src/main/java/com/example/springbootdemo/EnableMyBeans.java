package com.example.springbootdemo;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
/**
 * 将MyBeansConfiguration导入IOC容器中
 */
@Import(MyImportSelector.class)
public @interface EnableMyBeans {

    String value() default "";

    boolean enableMyImportSelector() default true;

}
