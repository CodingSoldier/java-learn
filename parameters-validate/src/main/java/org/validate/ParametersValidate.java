package org.validate;

import java.lang.annotation.*;


//运行时执行
@Retention(RetentionPolicy.RUNTIME)
//用于描述类、接口(包括注解类型) 或enum声明
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DAnnoLog {
    public String operationType() default "";
    public String operationName() default "";
}
