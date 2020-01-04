package com.mmall.concurrency.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-20
 */
public class B_ThreadPoolExecutor {


    private ThreadPoolExecutor pool = null;


    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----1
     * maximumPoolSize 最大线程池大小----3
     *
     * keepAliveTime 线程池中超过maximumPoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(5)====5容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     *                          即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     *                                任务会交给RejectedExecutionHandler来处理
     *
     * 提交任务，任务数小于最小线程数，创建新线程
     * 当workQueue满了之后，线程池才会从最小核心数增加线程到最大线程数
     *
     * 线程池的线程空闲时是 WAIT状态
     * 当线程池线程数超过corePoolSize，之后线程数减少，最少减为corePoolSize个
     *
     */
    //public void init() {
    //    pool = new ThreadPoolExecutor(
    //            3,
    //            5,
    //            30,
    //            TimeUnit.SECONDS,
    //            new ArrayBlockingQueue<Runnable>(2));
    //}


    /**
     * 线程池可选择的饱和策略
     * AbortPolicy 终止策略（默认）
     * DiscardPolicy 抛弃策略
     * DiscardOldestPolicy 抛弃旧任务策略
     * CallerRunsPolicy 调用者运行策略
     *
     * new ThreadPoolExecutor.DiscardPolicy()
     * 20个任务只有7个运行，其他任务被抛弃，但是没抛异常
     *
     * AbortPolicy 发生终止策略时，是运行pool.execute()的线程抛出异常RejectedExecutionException
     * 线程池不会因抛出RejectedExecutionException而不可用，后面通过另一个线程调用pool.execute()还是能运行成功的
     */

    //public void init() {
    //    pool = new ThreadPoolExecutor(
    //            3,
    //            5,
    //            30,
    //            TimeUnit.SECONDS,
    //            new ArrayBlockingQueue<Runnable>(2),
    //            new ThreadPoolExecutor.DiscardPolicy());
    //}

    public void init() {
        pool = new ThreadPoolExecutor(
                3,
                5,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2));
    }

    public ExecutorService getCustomThreadPoolExecutor() {
        return this.pool;
    }


    public static void main(String[] args) throws Exception{

        B_ThreadPoolExecutor exec = new B_ThreadPoolExecutor();
        exec.init();

        ExecutorService pool = exec.getCustomThreadPoolExecutor();
        for(int i=1; i<20; i++) {
            System.out.println("提交第" + i + "个任务!");
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(">>>task is running=====");
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
