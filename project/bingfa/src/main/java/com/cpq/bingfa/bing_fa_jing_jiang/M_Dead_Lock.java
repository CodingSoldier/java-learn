package com.cpq.bingfa.bing_fa_jing_jiang;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class M_Dead_Lock {
}

/**
 * 死锁的4个必要条件
 *   1、互斥条件
 *   2、保持与请求条件。请求一把锁的时候还保持着另一把锁
 *   3、循环等待条件。线程的锁之间存在等待环路
 *   4、不剥夺条件。没有外界条件干扰锁的获取与释放，举例子说明就是踢足球没有裁判
 * 4个条件缺一不可
 *
 * 避免死锁的思路：
 *   1、避免相反的获取锁顺序
 */
class DeadLock_Test implements Runnable{

    int flag = 1;
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    @Override
    public void run() {
        try {
            if (flag == 1) {
                synchronized (lock1) {
                    Thread.sleep(500);
                    synchronized (lock2) {
                        System.out.println("线程1成功拿到两把锁");
                    }
                }
            }else if (flag == 0) {
                synchronized (lock2) {
                    Thread.sleep(500);
                    synchronized (lock1) {
                        System.out.println("线程2成功拿到两把锁");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        DeadLock_Test dl1 = new DeadLock_Test();
        DeadLock_Test dl2 = new DeadLock_Test();
        dl1.flag=1;
        dl1.flag=0;
        Thread t1 = new Thread(dl1);
        Thread t2 = new Thread(dl2);
        t1.start();
        t2.start();
        Thread.sleep(1000);

        /**
         * 使用ThreadMXBean发现死锁
         */
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] dlt = threadMXBean.findDeadlockedThreads();
        if (dlt != null){
            for (int i=0; i<dlt.length; i++){
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(dlt[i]);
                System.out.println("发现死锁 "+threadInfo.getThreadName());
            }
        }

        ///**
        // * ThreadMXBean在任意线程中都有效
        // */
        //new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        //        long[] dlt = threadMXBean.findDeadlockedThreads();
        //        if (dlt != null){
        //            for (int i=0; i<dlt.length; i++){
        //                ThreadInfo threadInfo = threadMXBean.getThreadInfo(dlt[i]);
        //                System.out.println("新线程，发现死锁 "+threadInfo.getThreadName());
        //            }
        //        }
        //    }
        //}).start();
    }
}


class DeadLock_Test2 implements Runnable{

    static Object lock0 = new Object();

    static Object lock1 = new Object();
    static Object lock2 = new Object();

    public static void main(String[] args) throws Exception{
        DeadLock_Test dl1 = new DeadLock_Test();
        DeadLock_Test dl2 = new DeadLock_Test();
        Thread t1 = new Thread(dl1);
        Thread t2 = new Thread(dl2);
        t1.start();
        t2.start();
        Thread.sleep(1000);

        /**
         * 使用ThreadMXBean发现死锁
         */
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] dlt = threadMXBean.findDeadlockedThreads();
        if (dlt != null){
            for (int i=0; i<dlt.length; i++){
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(dlt[i]);
                System.out.println("发现死锁 "+threadInfo.getThreadName());
            }
        }
    }

    /**
     * 这种比较lock哈希值的方式还是有问题，因为锁是固定的
     */
    @Override
    public void run() {
        try {
            int lock1Hash = System.identityHashCode(lock1);
            int lock2Hash = System.identityHashCode(lock2);
            if (lock1Hash > lock2Hash) {
                synchronized (lock1) {
                    Thread.sleep(500);
                    synchronized (lock2) {
                        System.out.println("lock1向lock2转账完毕");
                    }
                }
            }else if (lock2Hash > lock1Hash) {
                synchronized (lock2) {
                    Thread.sleep(500);
                    synchronized (lock1) {
                        System.out.println("lock2向lock1转账完毕");
                    }
                }
            }else {
                synchronized (lock0){
                    synchronized (lock1) {
                        Thread.sleep(500);
                        synchronized (lock2) {
                            System.out.println("lock1向lock2转账完毕");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

















