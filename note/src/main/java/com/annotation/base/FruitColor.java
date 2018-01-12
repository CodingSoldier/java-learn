package com.annotation.base;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {

    public enum Color{BULE,RED,GREEN};

    Color fruitColor() default Color.RED;

}