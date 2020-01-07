package com.mmall.concurrency.bing_fa_jing_jiang;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class E_Interrupt_Stop_While {

static class Producer implements Runnable{

    BlockingQueue storage;

    public Producer(BlockingQueue storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;

        /**
         * 使用interrupt()设置中断标记位，并使用 Thread.currentThread().isInterrupted() 作为while的判断条件
         * while中必须要有阻塞操作，以便执行interrupt()的时候，阻塞方法抛出InterruptedException
         * catch异常必须在while的外部，不然catch异常后，while会继续运行
         */
        try {
            while (num < 10000 && !Thread.currentThread().isInterrupted()){
                if (num % 100 ==0){
                    storage.put(num);
                    System.out.println(num + "是100的倍数，被放到仓库中了。");
                }
                num++;
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            System.out.println("生产者结束");
        }

        /**
         * 这个代码有问题，catch写在了while循环中，
         * 中断的异常被捕获，并且抛出InterruptedException后中断标记位也会重置为false
         * 这就导致while循环不受影响，继续循环
         */
        //while (num <= 10000 && !Thread.currentThread().isInterrupted()){
        //    if (num % 100 == 0){
        //        try {
        //            storage.put(num);
        //        }catch (InterruptedException e){
        //            e.printStackTrace();
        //        }finally {
        //            System.out.println("生产者结束");
        //        }
        //        System.out.println(num + "是100的倍数，被放到仓库中了。");
        //    }
        //    num++;
        //}

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

        pt.interrupt();
    }


}