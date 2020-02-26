package com.cpq.bingfa.bing_fa_jing_jiang;


public class I_Catch_Thread_Exception {
    /**
     * 线程未捕获异常处理器接口 Thread.UncaughtExceptionHandler
     * 此接口会在线程由于未捕获异常而突然终止时被调用
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
         * 设置默认未捕获异常处理器
         */
        Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler("自定义未捕获异常处理器"));

        new Thread(new Runnable() {
            /**
             * Runnable接口中的run方法没有抛出受检查异常，实现类的run方法也就只能抛出不受检查的异常RuntimeException
             */
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
