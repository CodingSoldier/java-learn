package com.spring.iocdemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/10
 */
//运行时执行
@Retention(RetentionPolicy.RUNTIME)
//适用于成员变量、方法
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ResourceCustom {
    public String value() default "";
    public String name() default "";
}
