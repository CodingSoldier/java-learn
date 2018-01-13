package com.spring.aoplog;

import java.lang.annotation.*;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/13
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    public String operationType() default "";
    public String operationName() default "";
}
