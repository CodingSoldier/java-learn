package com.cpq.bingfa.threadpool;

import java.util.concurrent.*;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-20
 */
public class B_ThreadPoolExecutor {

    private ThreadPoolExecutor pool = null;

    /**
     * 提交任务，任务数小于最小线程数，创建新线程
     * 当workQueue满了之后，线程池才会从最小核心数增加线程到最大线程数
     *
     * 新增线程的判断顺序
     *    判断线程是否超过corePoolSize
     *    corePoolSize满了，判断workQueue
     *    workQueue满了，判断maxPoolSize
     *
     * 线程池的线程空闲时是 WAIT状态
     * 当线程池线程数超过corePoolSize，之后线程数减少，最少减为corePoolSize个
     *
     * 不要用Executors工厂类，因为可能最大线程数未限制或者队列为无界队列
     * 使用ThreadPoolExecutor自定义线程池
     *
     * 计算机密集型：cpq核数加1
     * IO密集型（网络请求多）：cpq核数/0.8 或者 cpq核数/0.9
     *
     * 用bean来创建线程池，在bean销毁时调用线程池的shutdown方法。
     * 在服务关闭的时候调用线程池的shutdown方法
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
                new ArrayBlockingQueue<Runnable>(20));
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
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();
    }
}




class ThreadPool{

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2));

    public static void main(String[] args) throws Exception{

        for (int i = 0; i < 10; i++) {
            // 因为拒绝策略而抛出异常，threadPoolExecutor.execute()也会抛异常
            try {
                threadPoolExecutor.execute(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }catch (Exception e){
                System.out.println("#######"+e.toString());
            }
        }

    }

}
