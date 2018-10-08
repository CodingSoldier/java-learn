package com.demo.paramsvalidate;

public class PvLevel {

    private PvLevel() {}

    public static final String STRICT = "STRICT";  //严格模式，发生异常，校验不通过，默认
    public static final String LOOSE = "LOOSE";   //宽松模式，发生异常，不校验
}
