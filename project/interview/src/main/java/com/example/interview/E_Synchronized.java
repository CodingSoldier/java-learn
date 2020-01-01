package com.example.interview;

public class E_Synchronized {

/**
 Java对象头和Monitor是实现synchronized的基础
 java对象头：
    Mark Word 默认存储对象的hashCode、分代年龄、锁类型、锁标志位等信息
        无锁状态 锁标志位01
        轻量级锁 锁标志位00
        重量级锁 锁标志位10
        偏向锁   锁标志位01 还包含是否偏向锁标志位1
    Class Metadata Address 类型指针指向对象的类元数据，JVM通过这个指针确定该对象是哪个类的数据
 Monitor：每个java对象天生自带一把看不见的锁
 */

    public static void main(String[] args) throws Exception{


    }
}
