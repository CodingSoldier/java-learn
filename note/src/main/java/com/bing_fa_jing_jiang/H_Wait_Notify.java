package com.bing_fa_jing_jiang;

import java.util.Date;
import java.util.LinkedList;
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















