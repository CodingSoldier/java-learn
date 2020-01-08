package com.cpq.bf.threadpool;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class C_QueueTest {

    @Test
    public void arrayBlockingQueue() throws InterruptedException {
        ArrayBlockingQueue<Integer> abq = new ArrayBlockingQueue(10);
        for (int i=0; i<20; i++){
            /**
             * 向abq中添加元素到达10个后，再次添加元素，线程阻塞了
             */
            abq.put(i);
            System.out.println("向队列添加值："+i);
        }
    }

    /**
     * 基于链表的阻塞队列
     * LinkedBlockingQueue构造函数不传初始值就是无界阻塞队列，ArrayBlockingQueue没有无参构造函数
     */
    @Test
    public void linkedBlockingQueue() throws InterruptedException {
        //LinkedBlockingQueue<Integer> lbq = new LinkedBlockingQueue(10);
        LinkedBlockingQueue<Integer> lbq = new LinkedBlockingQueue();
        for (int i=0; i<20; i++){
            /**
             * 向abq中添加元素到达10个后，再次添加元素，线程阻塞了
             */
            lbq.put(i);
            System.out.println("向队列添加值："+i);
        }
    }

    /**
     * 同步移交阻塞队列，用于生产者消费者模式
     */
    @Test
    public void synchronousQueue() throws InterruptedException {

        SynchronousQueue<Integer> sq = new SynchronousQueue(false);

        //new Thread(() -> {
        //    try {
        //        sq.put(1);
        //        System.out.println("生产成功"+1);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}).start();
        //new Thread(() -> {
        //    try {
        //        Integer i = sq.take();
        //        System.out.println("消费成功"+i);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}).start();


        new Thread(() -> {
            try {
                sq.put(1);
                System.out.println("生产成功"+1);


                sq.put(2);
                /**
                 * 给SynchronousQueue添加元素后，若消费者没有消费，则线程会在put方法内阻塞
                 * 调用的是 UNSAFE.park(false, 0L);
                 * take方法也一样，当SynchronousQueue没有数据，执行take方法，线程会阻塞，直到队列中添加了元素
                 */
                System.out.println("生产成功"+2);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                Integer i = sq.take();
                System.out.println("消费成功"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        TimeUnit.MINUTES.sleep(1);

    }

}
