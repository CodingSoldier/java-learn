package com.bing_fa_jing_jiang;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2019-12-13
 */
public class G_Synchronize {

    public static Object obj1 = new Object();
    public static Object obj2 = new Object();

    static class Thread1 extends Thread{
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            synchronized (obj1){
                System.out.println(threadName+" 获取锁 obj1");
                try {
                    System.out.println(threadName+" 锁obj1, sleep()");
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName+" 结束, 锁 obj1");
            }
            synchronized (obj2){
                System.out.println(threadName+" 获取锁 obj2");
                try {
                    System.out.println(threadName+" 锁obj2, sleep()");
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName+" 结束, 锁 obj2");
            }
        }
    }

    static class Thread2 extends Thread{
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            synchronized (obj1){
                System.out.println(threadName+" 获取锁 obj1");
                try {
                    System.out.println(threadName+" 锁obj1, sleep()");
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName+" 结束, 锁 obj1");
            }
            synchronized (obj2){
                System.out.println(threadName+" 获取锁 obj2");
                try {
                    System.out.println(threadName+" 锁obj2, sleep()");
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName+" 结束, 锁 obj2");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Thread thread1 = new Thread1();
        Thread thread2 = new Thread2();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }






    public static final Object lock = new Object();

    @Test
    public void synchronizeException() throws Exception{
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    if (lock != null){
                        System.out.println(name + " 抛出异常");
                        /**
                         * synchronized同步代码块内抛出异常，JVM会释放当前线程的锁
                         */
                        throw new RuntimeException();
                    }
                    try {
                        TimeUnit.MINUTES.sleep(3L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    System.out.println(name + " run");
                }
            }
        });

        thread1.start();
        TimeUnit.MILLISECONDS.sleep(10L);
        thread2.start();

        thread1.join();
        thread2.join();
    }


    /**
     * 可重入锁：同一个线程的外层函数获得锁之后，内层函数还可以再次直接获取该锁
     * synchronize是可重入锁
     *    在一个方法中是可重入的
     *    可重入不要求是同一个方法
     *    可重入不要求在同一个类中
     */
    @Test
    public void synchronizeKeChongRu() throws Exception{

        ///**
        // * 在一个方法中是可重入的
        // */
        //Thread thread1 = new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        String name = Thread.currentThread().getName();
        //        synchronized (lock){
        //            System.out.println(name + " 获取锁");
        //            try {
        //                TimeUnit.SECONDS.sleep(3L);
        //
        //                synchronized (lock){
        //                    System.out.println(name + " 在同一个锁中再次获取该锁");
        //                    TimeUnit.SECONDS.sleep(3L);
        //                }
        //
        //            } catch (InterruptedException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //    }
        //});
        //Thread thread2 = new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        String name = Thread.currentThread().getName();
        //        synchronized (lock){
        //            System.out.println(name + " run");
        //        }
        //    }
        //});
        //thread1.start();
        //TimeUnit.MILLISECONDS.sleep(10L);
        //thread2.start();
        //
        //thread1.join();
        //thread2.join();


        ///**
        // * 可重入不要求是同一个方法
        // */
        //Thread thread1 = new Thread(new Runnable() {
        //    void method1() throws InterruptedException{
        //        String name = Thread.currentThread().getName();
        //        synchronized (lock){
        //            System.out.println(name + " method1....");
        //            TimeUnit.SECONDS.sleep(2L);
        //            method2();
        //        }
        //    }
        //    void method2() throws InterruptedException{
        //        String name = Thread.currentThread().getName();
        //        synchronized (lock){
        //            System.out.println(name + " method2....");
        //            TimeUnit.SECONDS.sleep(2L);
        //        }
        //    }
        //    @Override
        //    public void run() {
        //        String name = Thread.currentThread().getName();
        //        synchronized (lock){
        //            System.out.println(name + " 可重入，不同方法使用同一个锁");
        //            try {
        //                method1();
        //            } catch (InterruptedException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //    }
        //});
        //Thread thread2 = new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        String name = Thread.currentThread().getName();
        //        synchronized (lock){
        //            System.out.println(name + " run");
        //        }
        //    }
        //});
        //thread1.start();
        //TimeUnit.MILLISECONDS.sleep(10L);
        //thread2.start();
        //
        //thread1.join();
        //thread2.join();


        /**
         * 可重入不要求是同一个类
         * 子类覆盖父类的synchronize方法，并且在同步方法内调用父类方法
         */
        class Thread1{
            public synchronized void method1(){
                System.out.println("父类方法");
            }
        }
        class Thread2 extends Thread1{
            @Override
            public synchronized void method1(){
                System.out.println("子类方法");
                super.method1();
            }
        }
        new Thread2().method1();
    }


    /**
     * synchronized不可中断：一旦锁被别人获取了，我只能选择阻塞，直到别的线程释放这个锁。
     * 如果别人永远不释放，那我只能永远的等下去。
     *
     * 当线程处于Blocked状态，调用interrupt()设置中断标记位，Blocked线程不会终止线程，还是一直阻塞直到获取锁
     */
    @Test
    public void synchronizeNotInterrupt() throws Exception{
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    try {
                        System.out.println(name + " run");
                        TimeUnit.SECONDS.sleep(3L);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    System.out.println(name + " run");
                }
            }
        });
        thread1.start();
        TimeUnit.MILLISECONDS.sleep(10L);
        thread2.start();
        TimeUnit.MILLISECONDS.sleep(10L);
        System.out.println("设置thread2中断标记位，thread2没中断。");
        thread2.interrupt();


        thread1.join();
        thread2.join();
    }

    /**
     * synchronized原理：
     *    每个java
     */


}
