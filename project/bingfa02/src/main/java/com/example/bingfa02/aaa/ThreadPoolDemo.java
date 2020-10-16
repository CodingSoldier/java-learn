package com.example.bingfa02.aaa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolDemo {
}

/*
使用线程池有以下好处：
1、线程的创建销毁会消耗CPU资源，如果一个线程（线程A）执行完任务后，不销毁此线程，等待下一个需要线程的任务到来，直接使用线程A执行此任务，就能避免线程新建、销毁的开销。
2、使用自己定义的线程池，可以合理设置线程数量，避免创建过多线程而占用太多CPU资源。
3、并且线程池中有一个存储任务的队列，这类似一个缓冲功能，避免

线程池（Thread Pool）是一种基于池化思想管理线程的工具。可以想象成把一些线程放到一个“池子”中，统一管理。
Java封装了一些线程池给开发者使用
Executors.newSingleThreadExecutor()  创建一个只有一个线程的线程池
Executors.newFixedThreadPool(int nThreads)  创建具有指定数量线程数的线程池
Executors.newCachedThreadPool()   创建一个线程池，线程池中的线程空闲时间超过60秒就销毁线程




假设一种场景：线程的创建销毁会消耗CPU资源，如果一个线程（线程A）执行完任务后，不销毁此线程，等待下一个需要线程的任务到来，直接使用线程A执行此任务，就能避免线程新建、销毁的开销。可以把多个线程统一管理起来，这种管理线程的工具就叫做线程池。





 */

class JavaThreadPool{

    public static void main(String[] args) throws Exception{
        // 创建线程池
        ExecutorService es = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                System.out.println(Thread.currentThread().getName()+" 线程运行");
            });
        }
        // 关闭线程池
        es.shutdown();

        new ThreadPoolExecutor()
    }

}





















