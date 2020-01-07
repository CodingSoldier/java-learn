package com.bf.bing_fa_jing_jiang;

import java.util.concurrent.atomic.AtomicInteger;

public class K_JMM {
}


/**
 * volatile两点作用：
 * 1、可见性：
 *      写volatile变量后，变量会立即刷入到主内存
 *      读取volatile之前，会将本地缓存（即本线程缓存）失效，然后再从主内存读取
 * 2、禁止指令重排序
 */
class VolatileBoolean{

    static volatile boolean done = false;
    static AtomicInteger atomicNum = new AtomicInteger();

    public static void main(String[] args) throws Exception{
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<10000000; i++){
                    done = !done;
                    atomicNum.incrementAndGet();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<10000000; i++){
                    /**
                     * 即便使用volatile修饰boolean done，但done也只支持done=true/false
                     * 不支持 done=!true/false
                     */
                    done = !done;
                    atomicNum.incrementAndGet();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("atomicNum永远是20000000，但是done可能是true、false");
        System.out.println("atomicNum: "+atomicNum+", done: "+done);
    }
}


/**
 * java中的原子操作：
 *     1、java虚拟机规范中除了long、double之外的基本类型（int、byte、boolean、short、char、float）的赋值操作
 *        long、double是64位的，写入的时候分两次写入，一次写32位。但是商用虚拟机已经考虑到这点，long和double的赋值都是原子性的
 *     2、所有应用reference的赋值操作，32位、64位系统都是
 *     3、Atomic包中的所有类的原子操作
 */















