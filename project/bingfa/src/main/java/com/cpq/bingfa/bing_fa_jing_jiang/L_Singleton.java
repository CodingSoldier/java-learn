package com.cpq.bingfa.bing_fa_jing_jiang;

public class L_Singleton {
}

class SingletonHunger{
    /**
     * 在类加载的时候就创建实例，即便INSTANCE还没被使用，也创建实例，这种方式叫做饿汉式。
     * jvm加载类的时候能保证线程安全，直接避免了多线程步问题
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
 *      分配内存、运行construct方法、内存地址赋值给变量
 *      这三个步骤可能会重排序为
 *      分配内存、内存地址赋值给变量、运行construct方法
 *      这样会导致执行到 内存地址赋值给变量，instance变量不为null，但是instance对象还没初始化完成
 *   2、使用volatile能够防止这个3个步骤的指令重排序
 *   3、并且volatile能够保证变量的可见性，防止线程一已经将对象赋值给变量，但是线程二读取变量仍然得到空
 */
class SingleLazy{
    /**
     * 类加载的时候不创建对象，instance为null。等到首次被调用的时候才会创建实例，这种方式叫懒汉式
     */
    // 错误定义instance的方式
    private static SingleLazy instance;

    // 正确定义instance的方式
    //private static volatile SingleLazy instance;

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


class SingletonObj {
    private SingletonObj() {
    }

    public static SingletonObj getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 静态内部类
     * 外部类加载的时候，内部类和静态内部类不会被加载，也就不会创建SingletonObj实例，实现了懒加载
     * 静态内部类只会被加载一次，能保证静态内部类成员变量初始化的线程安全
     */
    private static class SingletonHolder {
        private static final SingletonObj INSTANCE = new SingletonObj();
    }
}


/**
 * 枚举实现单例
 * 枚举类型是线程安全的，并且只会装载一次。并且能防止反序列化破坏单例
 */
enum SingleEnum{

    INSTANCE;

    public void method1(){
        System.out.println("业务代码");
    }
}

class UseSingleEnum {
    public static void main(String[] args) {
        SingleEnum.INSTANCE.method1();
    }
}












