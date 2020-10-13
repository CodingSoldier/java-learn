package com.cpq.bingfa.bing_fa_jing_jiang;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class B_Start_Run {

    @Test
    public void startrun() throws Exception{
        Runnable runnable = () -> {
            System.out.println("运行了");
            System.out.println(Thread.currentThread().getName());
        };

        // main线程直接调用Runnable.run()
        // 这种方法是错误的，没有意义。如果这样用，那还不如直接把run()方法代码写在本方法中
        // 我们创建Runnable的目的就是为了用多线程并行执行多个任务
        runnable.run();

        // 新建线程去调用Runnable.run()
        Thread thread = new Thread(runnable);
        thread.start();

        /**
         * 线程不能连续调用两次start()，因为线程start后就变成了其他状态，在start()源码中有if判断，若线程状态不是未启动状态，就抛出异常
         * start()其实使用的是native虚拟机本地方法
         */
        //thread.start();

        TimeUnit.SECONDS.sleep(5);
    }


}