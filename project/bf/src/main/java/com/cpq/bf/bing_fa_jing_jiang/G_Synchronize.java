package com.cpq.bf.bing_fa_jing_jiang;

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
    public void se() throws Exception{
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    if (lock != null){
                        /**
                         * synchronized同步代码块内抛出异常，JVM会释放当前线程的锁
                         */
                        throw new RuntimeException(name + " 抛出异常");
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
     *
     * 可重入原理：加锁次数计数器
     *     JVM负责跟踪对象被加锁的次数
     *     线程第一次给对象加锁的时候，计数器变为1，相同线程在此对象上再次获得锁时，计数器会递增
     *     当任务离开时，计数器递减，当计数为0时，锁被释放
     */
    @Test
    public void synchronizeKeChongRu() throws Exception{
        //
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


        ///**
        // * 可重入不要求是同一个类
        // * 子类覆盖父类的synchronize方法，并且在同步方法内调用父类方法
        // */
        //class Thread1{
        //    public synchronized void method1(){
        //        System.out.println("父类方法");
        //    }
        //}
        //class Thread2 extends Thread1{
        //    @Override
        //    public synchronized void method1(){
        //        System.out.println("子类方法");
        //        super.method1();
        //    }
        //}
        //new Thread2().method1();

    }


    /**
     * synchronized不可中断：一旦锁被别人获取了，我只能选择阻塞，直到别的线程释放这个锁。
     * 如果别人永远不释放，那我只能永远的等下去。
     *
     * 当线程处于Blocked状态，调用interrupt()设置中断标记位，Blocked线程不会终止线程，还是一直阻塞直到获取锁
     */
    @Test
    public void synchronizeNotInterrupt() throws Exception{
        Thread thread0 = new Thread(new Runnable() {
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
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    System.out.println(name + " run");
                }
            }
        });
        thread0.start();
        TimeUnit.MILLISECONDS.sleep(10L);
        thread1.start();
        TimeUnit.MILLISECONDS.sleep(10L);

        System.out.println("设置thread1中断标记位，thread1没中断。");
        thread1.interrupt();
        System.out.println("thread1.interrupt()执行后");


        thread0.join();
        thread1.join();
    }


    @Test
    public void threadRunException() throws Exception{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (lock){
                    try {
                        System.out.println(name + " run");
                        TimeUnit.SECONDS.sleep(3L);
                    }catch (Exception e){
                        throw new RuntimeException("线程中抛出异常，是否影响主线程运行？");
                    }
                }
            }
        });

        thread.start();
        TimeUnit.MILLISECONDS.sleep(10L);

        System.out.println("设置thread中断标记位");
        thread.interrupt();

        thread.join();
        System.out.println("主线程运行到最后，子线程抛出异常不会终止主线程");
        // 如果子线程抛出异常会导致主线程终止，那这种设计也缺陷也太严重了
    }


}
