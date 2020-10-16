package com.cpq.bingfa.bing_fa_jing_jiang;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2020-10-14
 */
public class Test01 {
}


class DeadLockDemo implements Runnable{

    int flag = 1;
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+"线程运行");
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
        DeadLockDemo dl1 = new DeadLockDemo();
        DeadLockDemo dl2 = new DeadLockDemo();
        dl1.flag=1;
        dl1.flag=0;
        Thread t1 = new Thread(dl1);
        Thread t2 = new Thread(dl2);
        t1.start();
        t2.start();

        // 睡眠1秒钟确保死锁发生
        TimeUnit.SECONDS.sleep(1);

        /**
         * 使用ThreadMXBean发现因死锁而阻塞的线程
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
}