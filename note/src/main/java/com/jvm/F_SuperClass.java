package com.jvm;

// P 211
public class F_SuperClass {
    static {
        System.out.println("SuperClass init!");
    }
    public static int value = 123;
}

class SubClass extends F_SuperClass{
    static {
        System.out.println("SubClass init !");
    }
}

class NotInitialzation{
    public static void main(String[] args){
        //初始化了F_SuperClass、未初始化SubClass
        System.out.println(SubClass.value);
    }
}

class NotInitialzation2{
    public static void main(String[] args){
        //未初始化F_SuperClass
        F_SuperClass[] sca = new F_SuperClass[10];
    }
}


// P213
class ConstClass{
    static {
        System.out.println("ConstClass init!");
    }
    public static final String HELLOWORLD = "hello world";
}

class NotInitialzation3{
    public static void main(String[] args){
        //常量会在编译阶段存入调用类的常量池中，本质上没有直接引用到定义常量的类，因此不会触发类的初始化
        System.out.println(ConstClass.HELLOWORLD);
    }
}

