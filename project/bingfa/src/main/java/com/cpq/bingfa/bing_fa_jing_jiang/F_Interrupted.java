package com.cpq.bingfa.bing_fa_jing_jiang;


public class F_Interrupted {

    public static void main(String[] args) throws InterruptedException {

        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                }
            }
        });

        // 启动线程
        threadOne.start();
        // 设置中断标志
        threadOne.interrupt();
        // 获取中断标志并重置
        /**
         * interrupted()获取中断标志并重置
         * Thread.interrupted()方法的目标对象是“当前线程”，而不管本方法来自于哪个对象
         * 源码 return currentThread().isInterrupted(true);
         * interrupted()是一个静态方法，获取的当然就是当前线程的中断标记位
         * 写成 obj.interrupted()完全就是为了迷惑人
         */
        System.out.println("isInterrupted: " + Thread.interrupted());
        // 获取中断标志
        System.out.println("isInterrupted: " + threadOne.isInterrupted());
        threadOne.join();
        System.out.println("Main thread is over.");

    }
}
