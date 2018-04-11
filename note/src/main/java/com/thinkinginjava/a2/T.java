package com.thinkinginjava.a2;

import com.thinkinginjava.a1.I;
import com.thinkinginjava.a1.II;

//P347
public class T {
    public static void main(String[] args){
        I i = new II();
        II ii = (II)i;
        ii.f();
        //ii.f2();  //实现类中是protected权限
    }
}
