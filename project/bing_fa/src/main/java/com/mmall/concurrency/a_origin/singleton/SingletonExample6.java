package com.mmall.concurrency.a_origin.singleton;

import com.mmall.concurrency.annoations.ThreadSafe;

/**
 * 饿汉模式
 * 单例实例在类装载时进行创建
 */
@ThreadSafe
public class SingletonExample6 {

    // 私有构造函数
    private SingletonExample6() {

    }

    static {
        instance = new SingletonExample6();
    }

    // 单例对象，有bug
    //private static SingletonExample6 instance = null;
    // 单例对象，无bug
    private static SingletonExample6 instance;

    // 静态的工厂方法
    public static SingletonExample6 getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance().hashCode());
        System.out.println(getInstance().hashCode());
    }


}
