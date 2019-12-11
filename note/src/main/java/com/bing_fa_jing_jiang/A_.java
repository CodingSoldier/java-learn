package com.bing_fa_jing_jiang;

import org.junit.Test;

/**
 * @author chenpiqian
 * @date: 2019-12-10
 */


public class A_ {

    /**
     * 实现Runnable接口
     * 这种方式更好
     * 1、接口可以多实现，子类不能多继承
     * 2、线程池可接收Runnable
     * 3、
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


    











}