package com;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//P316
public class K_K {
    static Logger logger = LoggerFactory.getLogger(K_K.class);
    @Test
    public void test01(){
        System.out.println(Integer.valueOf("154645432") == Integer.valueOf("154645432"));
        System.out.println(Integer.valueOf("127") == Integer.valueOf("127"));
        System.out.println(Integer.valueOf("-129") == Integer.valueOf("-129"));
    }
    public static void main(String[] args) {
        String a1 = null;
        logger.info("{}", a1);
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        int ei = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);  //封装类 == 比较，比较地址。-128<= 封装类 < 127 的时候读取缓存中的封装类，地址相等
        System.out.println(e == f);  //封装类值大于127，都是重新new对象，地址不相等
        System.out.println(ei == f);  // + - * / == 都会自动拆箱，比较数值
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b)); //equals(Object obj) 形参是对象，a+b自动装箱，封装类比较
        System.out.println(g == (a + b));  //类型不一样也是自动拆箱，比较数值
        System.out.println(g.equals(a + b)); //封装类.equals()都是封装类比较
    }
}
