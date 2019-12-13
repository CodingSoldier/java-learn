package com.bing_fa_jing_jiang;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2019-12-10
 */
public class B_Start_Run {

    @Test
    public void startrun() throws Exception{
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName());
        };

        // main线程直接调用Runnable.run()
        // 这种方法是错误的，没有意义
        runnable.run();

        // 新建线程去调用Runnable.run()
        Thread thread = new Thread(runnable);
        thread.start();

        /**
         * 线程不能连续调用两次start()，因为线程start后就变成了其他状态，在start()源码中有if判断，若线程状态不是未启动状态，就抛出异常
         * start()其实使用的是native虚拟机本地方法
         */
        //thread.start();

        TimeUnit.MINUTES.sleep(1L);
    }














}