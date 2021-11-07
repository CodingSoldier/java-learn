package com.cpq.bingfa.bing_fa_jing_jiang;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2019-12-10
 */
public class C_Stop_Thread {

    //public static void main(String[] args) throws Exception{
    //    Thread t0 = new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //            int num = 0;
    //            while (num < Integer.MAX_VALUE){
    //                System.out.println(Thread.currentThread().getName() + "正在执行");
    //                System.out.println("中断状态："+Thread.currentThread().isInterrupted());
    //            }
    //        }
    //    });
    //    t0.start();
    //
    //    TimeUnit.MILLISECONDS.sleep(100);
    //
    //    // 执行t0.interrupt()，但是t0还在继续运行
    //    t0.interrupt();
    //}


    //public static void main(String[] args) throws Exception{
    //    Thread t0 = new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //            // 当前线程的中断状态为true则退出while循环
    //            while (!Thread.currentThread().isInterrupted()){
    //                System.out.println(Thread.currentThread().getName() + "正在执行");
    //            }
    //        }
    //    });
    //    t0.start();
    //
    //    TimeUnit.MILLISECONDS.sleep(100);
    //
    //    // 执行t0.interrupt()，t0的中断状态变为true
    //    t0.interrupt();
    //}


    //public static void main(String[] args) throws Exception{
    //    Thread t0 = new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //            while (!Thread.currentThread().isInterrupted()){
    //                try {
    //                    System.out.println(Thread.currentThread().getName() + "正在执行");
    //
    //                    // 在线程中加入sleep()这种可以响应中断的代码，并在while循环中把异常捕获
    //                    TimeUnit.MILLISECONDS.sleep(1);
    //                }catch (InterruptedException e){
    //                    // 发生InterruptedException之后，线程的中断状态会被变成false
    //                    e.printStackTrace();
    //                }
    //            }
    //        }
    //    });
    //    t0.start();
    //
    //    TimeUnit.MILLISECONDS.sleep(100);
    //
    //    t0.interrupt();
    //}


    //public static void main(String[] args) throws Exception{
    //    Thread t0 = new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //            try {
    //                while (!Thread.currentThread().isInterrupted()){
    //                    System.out.println(Thread.currentThread().getName() + "正在执行");
    //                    System.out.println("中断状态："+Thread.currentThread().isInterrupted());
    //                    // 在线程中加入sleep()这种可以响应中断的代码
    //                    TimeUnit.MILLISECONDS.sleep(1);
    //                }
    //            // 将捕获异常的代码放到while循环之外，捕获异常之后不再有循环代码
    //            }catch (InterruptedException e){
    //                e.printStackTrace();
    //            }
    //            System.out.println("中断状态：" + Thread.currentThread().isInterrupted());
    //        }
    //    });
    //    t0.start();
    //
    //    TimeUnit.MILLISECONDS.sleep(100);
    //
    //    t0.interrupt();
    //}

    // 停止生产标识
    public static volatile boolean stopProduce = false;

    public static void main(String[] args) throws Exception {
        // 资源队列，用于公共存储资源
        LinkedBlockingQueue lbq = new LinkedBlockingQueue(3);

        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                try {
                    while (i < Integer.MAX_VALUE && !stopProduce) {
                        System.out.println("生产 " + i);
                        /**
                         * 当LinkedBlockingQueue队列满了，put方法会将线程设置为WAIT状态
                         * 若没有消费者消费LinkedBlockingQueue中的资源，线程会一直在这里等待，没法运行while判断
                         */
                        lbq.put(i);
                        i++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t0.start();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (new Random().nextInt(100) > 5) {
                        // 消费资源
                        System.out.println("消费 " + lbq.take());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        TimeUnit.MILLISECONDS.sleep(10);

        // 设置停止生产标识为true，但是t0没有停止，还处于WAIT状态
        stopProduce = true;

        // 消费一个LinkedBlockingQueue的资源后，t0从WAIT状态变为RUNNABLE，执行while判断后退出循环，线程结束
        //lbq.take();
    }


    /**
     * java只能通过interrupt来通知线程停止线程，至于是否中断线程，由线程本身自己决定
     * 调用interrupt()仅仅是将线程的中断标记位设置为true，并不是中断线程的意思
     * <p>
     * 线程停止：
     * 1、run方法运行完了
     * 2、run方法中抛出异常
     */
    @Test
    public void interrupt() throws Exception {

        //Thread thread = new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        Integer num = 0;
        //        while (num < Integer.MAX_VALUE){
        //            if (num % 100 == 0){
        //                System.out.println("当前数字 " + num);
        //                System.out.println("当前数字长度 " + num.toString().length());
        //            }
        //            num++;
        //        }
        //    }
        //});
        //thread.start();
        //// @Test 线程睡眠一会儿，然后调用thread.interrupt()，但是thread线程没有暂停
        //TimeUnit.SECONDS.sleep(1L);
        //thread.interrupt();


        // 在run()中加入Thread.currentThread().isInterrupted()
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer num = 0;
                // 当线程发出中断信号，则跳出while循环，run方法马上运行结束，线程也就停止了
                while (!Thread.currentThread().isInterrupted()
                        && num < Integer.MAX_VALUE) {
                    if (num % 100 == 0) {
                        System.out.println("当前数字 " + num);
                        System.out.println("当前数字长度 " + num.toString().length());
                    }
                    num++;
                }
            }
        });
        thread.start();
        // @Test线程睡眠一会儿，然后调用thread.interrupt()，thread线程暂停了
        TimeUnit.SECONDS.sleep(1L);
        thread.interrupt();


        //Thread thread = new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        Integer num = 0;
        //        while (num < 100){
        //            System.out.println("执行一小段时间，当前数字 " + num);
        //            num++;
        //        }
        //
        //        /**
        //         *  线程处于sleep状态，thread.interrupt()可以中断线程
        //         *  正是由于sleep状态可以被中断，所以sleep方法抛出显式异常，告诉调用者要处理异常
        //         *  从异常信息也可以看出来，sleep响应中断就是抛出一个异常InterruptedException
        //         *  而catch这个异常后，run方法不会结束，还会继续运行后面的代码
        //         */
        //        try {
        //            TimeUnit.SECONDS.sleep(2L);
        //        }catch (InterruptedException e){
        //            e.printStackTrace();
        //        }
        //
        //        System.out.println("线程中断后，后面的代码也会会会会执行");
        //    }
        //});
        //thread.start();
        //TimeUnit.SECONDS.sleep(1L);
        //// 设置线程thread中断标记位为true
        //thread.interrupt();


        //Thread thread = new Thread(new Runnable() {
        //    /**
        //     * run方法不能抛出check exception
        //     * 因为run方法是一个@Override方法，接口方法没有抛出异常，实现类中就不能抛出check exception
        //     */
        //    @Override
        //    public void run(){
        //        Integer num = 0;
        //        while (num < 100 && !Thread.currentThread().isInterrupted()){
        //            System.out.println("执行一小段时间，当前数字 " + num);
        //            num++;
        //            try {
        //                /**
        //                 * sleep状态被中断，抛出线程InterruptedException，
        //                 * 抛出InterruptedException后，线程的中断标记位就会重置为false
        //                 * !Thread.currentThread().isInterrupted() 就变成了true
        //                 * 并且中断异常被catch了，while循环会继续执行
        //                 * 这是一种错误使用中断的方式
        //                 */
        //                TimeUnit.MILLISECONDS.sleep(100L);
        //            }catch (InterruptedException e){
        //                e.printStackTrace();
        //            }
        //        }
        //    }
        //});
        //thread.start();
        //TimeUnit.SECONDS.sleep(1L);
        //thread.interrupt();


        /**
         * java响应中断方法总结，可以点到这些方法中，抛出InterruptException异常的方法就能响应中断：
         * Object.wait()/wait(long)/wait(long, int)
         * Thread.sleep(long)/sleep(long, int)
         * Thread.join()/join(long)/join(long, int)
         * java.util.concurrent.BlockingQueue.take()/put(E)
         * java.util.concurrent.locks.Lock.lockInterruptibly()
         * java.util.concurrent.CountDownLatch.await()
         * java.util.concurrent.CyclicBarrier.await()
         * java.util.concurrent.Exchanger.exchange(V)
         * java.nio.channels.InterruptibleChannel
         * java.nio.channels.Selector
         */


        // 防止@Test线程退出
        TimeUnit.SECONDS.sleep(60L);
    }


}