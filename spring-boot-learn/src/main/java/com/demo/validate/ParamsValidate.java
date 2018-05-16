package com.demo.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//运行时执行
@Retention(RetentionPolicy.RUNTIME)
//用于描述类、接口(包括注解类型) 或enum声明
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ParamsValidate {
    public String file() default "";
    public String keyName() default "";
}
