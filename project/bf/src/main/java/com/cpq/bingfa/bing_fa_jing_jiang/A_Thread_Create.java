package com.cpq.bf.bing_fa_jing_jiang;

import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class A_Thread_Create {

    /**
     * 实现Runnable接口
     * 这种方式更好
     * 1、接口可以多实现，子类不能多继承
     * 2、线程池可接收Runnable
     */
    public class RunnableStyle implements Runnable{
        @Override
        public void run() {
            System.out.println("运行run方法");
        }
    }
    @Test
    public void runnable(){
        Thread thread = new Thread(new RunnableStyle());
        thread.start();
    }



    /**
     * 继承Thread类
     */
    class ThreadStyle extends Thread{
        @Override
        public void run() {
            System.out.println("继承thread类");
        }
    }
    @Test
    public void thread1(){
        new ThreadStyle().start();
    }



    @Test
    public void threadRunnable(){

        /**
         * 因为原始的Thread类中，run方法会调用传入的Runnable中的run
         * 而我们创建的匿名类覆盖了run方法，覆盖方法中并没有调用Runnable的run
         */
        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("runnable中的run()");
            }
        }){
            @Override
            public void run() {
                System.out.println("匿名类Thread中的run()");
            }
        }.start();
    }


    /**
     * 创建线程池
     */
    @Test
    public void pool(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i<1000; i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
    }


    /**
     * 定时器创建线程
     */
    @Test
    public void timer1() throws Exception{
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }, 1000, 1000);
        TimeUnit.MINUTES.sleep(1L);
    }

    
}