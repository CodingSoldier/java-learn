package com.bing_fa_jing_jiang;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class D_Volatile_Not_Stop_While {


static class Producer implements Runnable{

    public volatile boolean canceled = false;

    BlockingQueue storage;

    public Producer(BlockingQueue storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        /**
         * storage.put方法中有await()方法
         * while条件使用volatile boolean canceled 无法停止线程
         * 比如当阻塞队列BlockingQueue满了，storage.put方法就会执行await()方法，当前线程处于等待状态，线程阻塞。
         * 并且当BlockingQueue处于队列已满的状态，消费者在此时不消费，则生产者线程将一直阻塞。
         * 外部的main线程执行canceled=true也无法立即停止线程，因为队列满了，生产者线程一直处于等待状态
         * 只有当消费者再消费一个数据，生产者从等待状态变成运行状态，生产者线程不摘阻塞，while循环得以继续运行，while循环才会退出，生产者线程运行结束
         */
        while (num <= 10000 && !canceled){
            if (num % 100 == 0){
                try {
                    storage.put(num);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("生产者结束，但是这句话很可能不会马上执行");
                }
                System.out.println(num + "是100的倍数，被放到仓库中了。");
            }
            num++;
        }
    }
}

static class Consumer{
    BlockingQueue storage;

    public Consumer(BlockingQueue storage) {
        this.storage = storage;
    }

    public boolean canConsume(){
        return Math.random() > 0.95;
    }
}


    public static void main(String[] args) throws Exception {
        ArrayBlockingQueue storage = new ArrayBlockingQueue(10);
        Producer producer = new Producer(storage);
        Thread pt = new Thread(producer);
        pt.start();
        TimeUnit.SECONDS.sleep(1L);

        Consumer consumer = new Consumer(storage);
        while (consumer.canConsume()){
            System.out.println(consumer.storage.take()+"被消费");
            TimeUnit.MILLISECONDS.sleep(100L);
        }
        System.out.println("消费者停止消费");

        producer.canceled = true;
        System.out.println(producer.canceled);
        //可以在这里打断点，然后再消费一个数据，生产者线程就能退出了
    }



}