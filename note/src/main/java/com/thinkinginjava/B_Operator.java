package com.thinkinginjava;

import com.commonsjar.beanutils.User;
import org.junit.Test;

import java.util.Random;

public class B_Operator {

    @Test
    public void t(){
        String a = "aaa";
        a += "bbb";   //string也支持+=
        System.out.println(a);

        //算数运算符，先乘除后加减
        System.out.println(1+3*4/6-2);
        System.out.println(1+4/4*1000-2);

        //rand
        Random rand = new Random(47);
        int j = rand.nextInt(100)+1;
        System.out.println(j);

        int i = 1;
        System.out.println(i++);

        //用小写l容易和1混淆，使用大写L
        long L1 = 1L;
        System.out.println(L1);

    }

    @Test
    public void charTest(){
        char x = 'x';
        char y = 'y';
        System.out.println(x*y);
        System.out.println('2'*'6');
        //char c26 = x*y;  报错，char运算后是int
        char c26 = 'x'*'y';  //居然又不报错
        System.out.println(x/y);
        System.out.println(x%y);
        System.out.println(x+y);
        System.out.println(x-y);

        byte b1 = 1;
        byte b2 = 2;
        //byte b12 = b1+b2;  报错，byte运算后是int

        short s1 = 1;
        short s2 = 2;
        //short s12 = s1+s2;  报错，short运算后是int

        System.out.println(Float.MAX_VALUE);

        //不会报错，但结果是1，Integer最大值相乘已经超出了int的范围
        System.out.println(Integer.MAX_VALUE * Integer.MAX_VALUE);
    }

    public static final String h = "h";
    @Test
    public void teste(){
        String h1 = "h";
        String h2 = "h";
        System.out.println(h1 == h2);

        User user = new User();
        user.setId("h");
        System.out.println(A_String.TEST_H == user.getId());
    }

    //斐波那契数列
    public int fibonacciSequence(int i){
        if(i == 2){
            return 1;
        }else if(i < 2){
            return 1;
        }
        return fibonacciSequence(i-1) + fibonacciSequence(i-2);
    }

    @Test
    public void testf(){
        System.out.println(fibonacciSequence(9));
    }





}
