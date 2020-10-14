package com.cpq.bingfa.bing_fa_jing_jiang;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2020-10-14
 */
public class Test01 {

    public static final Object LOCK = new Object();

    public static void main(String[] args) throws Exception {
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK){
                try {
                    System.out.println("线程1等待");
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (LOCK){
                System.out.println("线程2运行");
                System.out.println("唤醒使用了同一个锁的线程");
                LOCK.notifyAll();
            }
        });

        thread1.start();
        TimeUnit.MILLISECONDS.sleep(100);
        thread2.start();
    }
}
