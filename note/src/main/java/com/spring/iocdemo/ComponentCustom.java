package com.spring.iocdemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：cpq
 * @Description： 自定义注解
 */
//运行时执行
@Retention(RetentionPolicy.RUNTIME)
//用于描述类、接口(包括注解类型) 或enum声明
@Target({ElementType.TYPE})
public @interface ComponentCustom {
    public String value() default "";
    public String name() default "";
}
