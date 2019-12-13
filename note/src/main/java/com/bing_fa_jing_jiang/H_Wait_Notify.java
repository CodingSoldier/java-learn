package com.bing_fa_jing_jiang;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2019-12-13
 */
public class H_Wait_Notify {

    public static Object obj = new Object();

    static class Thread1 extends Thread{
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            synchronized (obj){
                try {
                    System.out.println(threadName+"获取锁，并wait()");
                    /**
                     * wait()方法会释放锁，并让线程处于WAITING状态
                     * 线程被唤醒后继续执行后面的代码
                     * wait()、notify()、notifyAll()都是在同步代码块中通过锁对象来调用
                     *
                     * notifyAll()唤醒所有obj触发的WAITING、TIMED_WAITING线程
                     */
                    obj.wait();

                    /**
                     * sleep()不会释放锁
                     * 执行sleep()后，线程状态是TIMED_WAITING
                     */
                    //TimeUnit.SECONDS.sleep(11);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println(threadName + "结束");
        }
    }

    static class Thread2 extends Thread{
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            synchronized (obj){
                System.out.println(threadName+"获取锁");
                obj.notify();
                System.out.println(threadName+"结束");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Thread thread1 = new Thread1();
        Thread thread2 = new Thread2();
        thread1.start();
        TimeUnit.MILLISECONDS.sleep(100L);
        System.out.println(thread1.getState());
        thread2.start();
    }

}
