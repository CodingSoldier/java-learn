package com.mmall.concurrency.bing_fa_jing_jiang;

/**
 * @author chenpiqian
 * @date: 2019-12-19
 */
public class L_Singleton {
}

class SingletonHunger{

    /**
     * 饿汉式
     * 在类加载的时候就创建实例，这样直接避免了线程同步问题
     * jvm能保证类加载时线程安全
     */
    private final static SingletonHunger INSTANCE = new SingletonHunger();

    private SingletonHunger() {
    }

    public static SingletonHunger getInstance(){
        return INSTANCE;
    }

}

/**
 * 懒汉式单例，使用double-check
 * 为什么要用volatile：
 *   1、新建对象不是原子性的，实际上有3个步骤
 *      分配内存、内存地址赋值给变量、运行construct方法
 *   2、使用volatile能够防止这个3个步骤的指令重排序
 *   3、并且volatile能够保证变量的可见性，防止线程一已经将对象赋值给变量，但是线程二读取变量仍然得到空
 */
class SingleLazy{

    private static volatile SingleLazy instance;

    private SingleLazy() {
    }

    public static SingleLazy getInstance(){
        if (instance == null){
            synchronized (SingleLazy.class){
                if (instance == null){
                    instance = new SingleLazy();
                }
            }
        }
        return instance;
    }

}


/**
 * 使用枚举实现单例模式，优点
 *    写法简单
 *    线程安全
 *    懒加载
 *    避免反序列化破坏单例
 */
enum SingleEnum{

    INSTANCE;

    public void method1(){

    }

}

class useSingleEnum{
    public static void main(String[] args) {
        SingleEnum.INSTANCE.method1();
    }
}












