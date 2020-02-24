package com.example.bspringboot.l_starter.myconditional;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(MyOnPropertyCondition.class)
public @interface MyConditionalOnProperty {
    String[] value() default {};
}
