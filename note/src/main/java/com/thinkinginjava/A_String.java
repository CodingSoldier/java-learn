package com.thinkinginjava;

import org.junit.Test;

import java.math.BigDecimal;

public class A_String {

    public static void print(Object o){
        System.out.println(o.toString());
    }
    public static void printnb(Object o){
        System.out.println(o.toString());
    }

    public static final String TEST_H = "outFill";

    String[] arr1;
    char c1;

    //byte、short初始值，默认值包装类是null，byte、short基本类型是0
    Byte b001;  //null
    byte b002;  //0
    Short s001;  //null
    short s002;  //0
    Double d001; //null
    double d002; //0.0

    @Test
    public void bigdecimal(){
        //23、24p BigDecimal、BigInteger是包装类对象，不是基本类型，不能直接使用运算符
        BigDecimal d1 = new BigDecimal(100);
        BigDecimal d2 = new BigDecimal(100);
        //System.out.println(d1*d2);
        System.out.println(d1.multiply(d2));
        //有基本类型的包装类可以直接使用运算符，java会自动拆装箱
        Integer i1 = new Integer(10);
        Integer i2 = new Integer(10);
        System.out.println(i1*i2);

        System.out.println("************************");

        //arr1初始化为null?
        String[] arr2;
        System.out.println(arr1);
        //System.out.println(arr2);

        //26p java基本类型的成员进行初始化，c1初始化了。但是不会对局部变量初始化，直接使用c2，编译报错。
        char c2; //默认值'\u0000'是一个控制符，代表没有值，不可见。不是空格，真正的空额nicode编码是'\u0020'
        System.out.println("*"+c1+"*");
        //System.out.println(c2);

    }

    @Test
    public void t1(){
        System.getProperties().list(System.out);
        System.out.println("*******************************************************");
        System.out.println(System.getProperty("user.name"));
        System.out.println(System.getProperty("java.library.path"));
    }

    @Test
    public void t2(){
        byte by1 = 1;
        Byte by2 = by1;

        short sh1 = 1;
        Short sh2 = sh1;

        System.out.println(s001);
        System.out.println(s002);
        System.out.println(b001);
        System.out.println(b002);
        System.out.println(d001);
        System.out.println(d002);

    }

}
