package com.cpq.bingfa.interview;

public class D_Thread {

    public static void main(String[] args) throws Exception{
        final Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("AAAAAAAA 准备获取锁");
                synchronized (lock){
                    try {
                        System.out.println("AAAAAAAA 获取到锁，sleep20ms");
                        /**
                         * sleep不会释放锁
                         */
                        Thread.sleep(20);
                        System.out.println("AAAAAAAA wait10ms");
                        /**
                         * wait(时间)会释放锁，
                         * 等待时间到期后，应该是进入block状态，等待另一个线程释放锁
                         */
                        lock.wait(10);
                        System.out.println("AAAAAAAA 结束");
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Thread.sleep(10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("BBBBBB 准备获取锁");
                synchronized (lock){
                    try {
                        System.out.println("BBBBBB 获取到锁，sleep2000ms");
                        Thread.sleep(2000);
                        System.out.println("BBBBBB 结束");
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
