package com.example.springbootdemo;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Conditional(OnSystemAndPropertyCondition.class)
public @interface ConditionalOnSystemAndProperty {
    String name() default "";

    String value() default "";
}