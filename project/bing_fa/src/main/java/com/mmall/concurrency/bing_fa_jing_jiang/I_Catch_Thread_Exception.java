package com.mmall.concurrency.bing_fa_jing_jiang;

/**
 * @author chenpiqian
 * @date: 2019-12-17
 */
public class I_Catch_Thread_Exception {

    /**
     * 线程未捕获异常处理器
     */
    public static class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

        private String name;

        public CustomUncaughtExceptionHandler(String name) {
            this.name = name;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(t.getName()+" 发生异常，终止了");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        /**
         * 设置线程未捕获异常捕获器，捕获并处理全局异常
         */
        Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler("自定义捕获器"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        }, "线程-1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        }, "线程-2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        }, "线程-3").start();
    }

}
