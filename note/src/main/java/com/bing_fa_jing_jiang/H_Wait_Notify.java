package com.bing_fa_jing_jiang;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

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


/**
 * 生产者-消费者 模式
 */
class DateStorage{

    private LinkedList<Date> storage;
    private int maxSize = 10;

    public DateStorage() {
        this.storage = new LinkedList<Date>();
    }

    public synchronized void put(){
        // 存储满了，生产线程等待
        while (storage.size() == maxSize){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("生产商品，"+storage.add(new Date())+"。当前商品个数"+storage.size());
        // 唤醒其他线程
        notify();
    }

    public synchronized void take(){
        while (storage.size() == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("消费商品，"+storage.poll()+"。当前商品个数"+storage.size());
        // 唤醒其他线程
        notify();
    }
}

class DateProducer implements Runnable{

    private DateStorage storage;

    public DateProducer(DateStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i=0; i<100; i++){
            storage.put();
        }
    }
}

class DateConsumer implements Runnable{

    private DateStorage storage;

    public DateConsumer(DateStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i=0; i<100; i++){
            storage.take();
        }
    }
}

class DatePS{
    public static void main(String[] args) {
        DateStorage dateStorage = new DateStorage();
        DateProducer dateProducer = new DateProducer(dateStorage);
        DateConsumer dateConsumer = new DateConsumer(dateStorage);

        Thread pt = new Thread(dateProducer);
        Thread st = new Thread(dateConsumer);
        pt.start();
        st.start();
    }
}




class SleepDontReleaseLock implements Runnable{

    @Override
    public void run() {
        synchronized (this){
            String name = Thread.currentThread().getName();
            System.out.println(name+"线程，同步代码块开始");
            try {
                /**
                 * 在同步代码块中
                 * 线程sleep()不会释放锁，sleep()必须接收一个时间参数
                 * 线程在sleep()时间到了之后，线程会释放锁
                 *
                 * sleep()也不会释放LOCK接口同步代码块中的锁
                 */
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + "线程，同步代码块结束");
        }
    }

    public static void main(String[] args) {
        SleepDontReleaseLock s = new SleepDontReleaseLock();
        new Thread(s).start();
        new Thread(s).start();
    }

}


/**
 * join
 */
class JoinInterrupt{

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 子线程加入主线程，主线程是WAITING状态
                     * 主线程发生中断，主线程从WAITING状态变成RUNNABLE，主线程继续运行，
                     * 主线程结束，子线程不会结束，子线程继续运行它的run方法
                     */
                    System.out.println(mainThread.getState());
                    mainThread.interrupt();
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("子线程执行完毕");
                }catch (InterruptedException e) {
                    System.out.println("子线程中断发生中断");
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        try {
            System.out.println("子线程加入主线程");
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println("主线程中断异常");
            e.printStackTrace();
        }
        System.out.println("主线程运行完毕");
    }

}




class JoinPrinciple {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            }
        });

        thread.start();
        System.out.println("开始等待子线程运行完毕");

        //thread.join();

        /**
         * thread.join()的等价与下面的3行代码，join()方法内部就是调用了wait(0)
         * 当线程执行到同步代码块，由于thread.wait()导致当前线程等待，注意是当前main线程等待，而不是thread等待，可以将锁thread换成obj来理解
         * 并且当thread.run()执行完毕，jvm会唤醒锁等待的线程？
         */
        synchronized (thread){
            thread.wait();
        }

        System.out.println("所有子线程执行完毕");
    }
}











