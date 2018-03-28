package com.thinkinginjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

class Insect{
    private int i = 9;
    protected int j;
    Insect(){
        print("Insect()构造器执行");
        System.out.println("i="+i+"   j="+j);
        j=39;
    }
    private static int x1 = print("Insect的静态变量x1，初始化");
    static int print(String s){
        System.out.println(s);
        return 11;
    }
}
class Beetle extends Insect{
    private int k = print("Beetle的非非非静态变量k，初始化");
    public Beetle(){
        print("Beetle()构造器执行");
        print("k="+k);
        print("j="+j);
    }
    private static int x2 = print("Beetle的静态变量x2，初始化");
}
class JapaneseBeetle extends Beetle {
    int m = print("JapaneseBeetle的非非非静态变量m，初始化");
    JapaneseBeetle() {
        print("JapaneseBeetle()构造器执行");
        print("m = " + m);
        print("j = " + j);
    }
    static int x3 =print("JapaneseBeetle的静态变量x3，初始化");
}

public class E_Final {
    public static final List LIST_1 = new ArrayList();
    //LIST_1 = new ArrayList();   p140

    @Test
    public void t(){
        new JapaneseBeetle();
    }
}

//空白final  p142
class T{
    private final int i;
    T(){
        i=1;
    }
}


//143-144
class Fi{
    private final void f(){
        System.out.println("f");
    }
    final void f1(){
        System.out.println("f1");
    }
}

class Fi1 extends Fi{
    private final void f(){
        System.out.println("fi1#f");
    }
    //void f1(){  //报错
    //    System.out.println("fi1#f1");
    //}
}





















