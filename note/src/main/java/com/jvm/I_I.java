package com.jvm;

public class I_I {
    static int a;  //成员变量会赋初值
    public static void main(String[] args) throws Exception{
        int b;   //局部变量不会赋初值
        System.out.println(a);
        //System.out.println(b);   //编译失败
    }
}
