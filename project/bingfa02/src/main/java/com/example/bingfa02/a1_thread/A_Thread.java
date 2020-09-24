package com.example.bingfa02.a1_thread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class A_Thread {
    /**
     先查看 com.cpq.bingfa.threadpool.A_CustomThreadPoolExecutor
     */
}

class ThreadPoolTest{

    public static void main(String[] args) {
        /*
         固定大小线程池
         源码如下：
         new ThreadPoolExecutor(nThreads, nThreads, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
             1、核心线程数与最大线程数一样
             2、keepAliveTime时间为0
             3、使用无界队列new LinkedBlockingQueue<Runnable>()，有可能会有OOM异常
         */
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i=0; i<1000; i++){
            executorService.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }


        /*
         单线程线程池
         源码
         new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
         也是无界队列
         */
        Executors.newSingleThreadExecutor();


        /*
        可缓存的线程池
        源码
        new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
        线程空闲存活时间是60秒
        SynchronousQueue是一个内部只能包含一个元素的队列
        最大线程数太大了
         */
        Executors.newCachedThreadPool();

    }

    @Test
    public void test01() throws Exception{
        /*
        创建定时的线程池
         */
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2);

        //// 延迟2秒执行
        //threadPool.schedule(() -> {
        //    System.out.println(Thread.currentThread().getName());
        //}, 2, TimeUnit.SECONDS);

        /*
        scheduleAtFixedRate ，是以上一个任务开始的时间计时，period时间过去后，检测上一个任务是否执行完毕，如果上一个任务执行完毕，则当前任务立即执行，如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行。
         */
        threadPool.scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName());
        }, 1, 3, TimeUnit.SECONDS);

        // 防止退出
        TimeUnit.MINUTES.sleep(10);
    }

    /*
    线程池里的数量设定为多少合适：
    CPU密集型（加密、计算hash等）：线程数设置为CPU核心数的1-2被左右
    耗时IO型（读写数据库、文件、网络）：最佳线程数一般会大于CPU核心数很多倍，以JVM线程监控显示繁忙情况为依据，保证线程空闲可以衔接上。
        参考公式:
            线程数 = CPU核心数 * (1 + 线程平均等待时间/线程平均工作时间)

    计算IO密集型应用的最大线程数
        公式： CPU核心数 * (1 + 线程平均等待时间 / 线程平均工作时间)
        阻塞系数 = 阻塞时间 /（阻塞时间+计算时间）
        core / （1-阻塞系数） 和 core * ( 1 + w/c ) 这俩是同一公式
     */

    @Test
    public void t2(){
        /*
        Runtime.getRuntime().availableProcessors()
            返回Java虚拟机可用的处理器数量（永远不小于一个）。
            在虚拟机的特定调用期间，此值可能会更改。 因此，对可用处理器数量敏感的应用程序应该偶尔轮询此属性并适当地调整其资源使用情况。
         */
        System.out.println("JVM虚拟机可用处理器数量，"+Runtime.getRuntime().availableProcessors());
    }

}

class ShutdownThreadPool{

    public static void main(String[] args) throws Exception {
        ArrayList<Object> list = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {

                for (int j = 0; j < 10000000; j++) {
                    try {
                        list.set(1, j);
                    }catch (Exception e){
                    }
                }

                //try {
                //     TimeUnit.MILLISECONDS.sleep(10);
                //}catch (Exception e){
                //    e.printStackTrace();
                //}

                System.out.println(Thread.currentThread().getName());
            });
        }
        TimeUnit.MILLISECONDS.sleep(10);


        //System.out.println("线程池是否处于停止状态： "+ executorService.isShutdown());
        ///**
        // * 让线程池处于停止状态，直到线程池中的任务运行完毕。线程池才真正停止
        // */
        //executorService.shutdown();
        //System.out.println("线程池是否处于停止状态： "+ executorService.isShutdown());

        //System.out.println("线程池中的任务是否运行完毕： "+ executorService.isTerminated());


        /**
         * 立即让线程处于停止状态。
         * 正在运行的线程无法被停止，处于阻塞状态的线程会被中断
         *
         * 返回未运行的任务列表，返回任务列表不会被继续执行
         */
        List<Runnable> runnables = executorService.shutdownNow();
        System.out.println(runnables.toString());
        //System.out.println("线程池是否处于停止状态： "+ executorService.isShutdown());
    }

    /**
     * executorService.execute()源码分析
     *      ThreadPoolExecutor#execute(java.lang.Runnable)
     *          ThreadPoolExecutor#addWorker(java.lang.Runnable, boolean)
     *          将线程添加到Worker中
     *              class Worker extends AbstractQueuedSynchronizer implements Runnable
     *              Worker类实现了Runnable
     *                  ThreadPoolExecutor#runWorker(java.util.concurrent.ThreadPoolExecutor.Worker)
     *                  取出Work中的task运行
     *                      task.run();
     *                      task运行
     */

}


class MyPool extends ThreadPoolExecutor{

    public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 线程池还有一些钩子方法
     * @param t
     * @param r
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }
}


















