package com.mmall.concurrency.z_bingfa;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class A_AQS {

    public static void main(String[] args) {

        AtomicLong al = new AtomicLong(0);
        /**
         * 预期原始值是0，更新为2
         */
        boolean flag = al.compareAndSet(0, 2);
        System.out.println(flag);
        System.out.println(al.get());

        /**
         * 预期原始值是0，更新为2
         * 更新失败，因为原始值不是0了
         */
        flag = al.compareAndSet(0, 2);
        System.out.println(flag);
        System.out.println(al.get());

    }

    @Test
    public void test1(){
        /**
         * AQS 抽象同步队列 AbstractQueuedSynchronizer
         * 看A_AQS.jpg
         * AbstractQueuedSynchronizer中维护一个private volatile int state;
         * 当status==0则表明资源没上锁，使用compareAndSet(0, 1)如果成功，则当前线程成功获取锁
         * compareAndSet(0, 1)返回false，则获取锁失败
         *
         */
        ReentrantLock reentrantLock = new ReentrantLock();

    }

}
