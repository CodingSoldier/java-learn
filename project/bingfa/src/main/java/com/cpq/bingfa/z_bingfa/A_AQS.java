package com.cpq.bingfa.z_bingfa;

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
    public void test1() {
        /**
         * AQS 抽象同步队列 AbstractQueuedSynchronizer
         * 看A_AQS.jpg
         * AbstractQueuedSynchronizer中维护一个private volatile int state;
         * 当status==0则表明资源没上锁，使用compareAndSet(0, 1)如果成功，则当前线程成功获取锁
         * compareAndSet(0, 1)返回false，则获取锁失败
         *
         * 公平锁，如果state == 0，则当前线程获取锁，如果state != 0，则将线程放入队列排队
         * 非公平锁，直接 compareAndSet(0,1)成功了，则获取到锁，没成功，转公平锁逻辑
         *
         */
        ReentrantLock reentrantLock = new ReentrantLock();

        /**
         * CountDownLatch，任务分成N个子线程去执行，state也初始化为N
         * 每个子线程执行完后countDown()一次，state会CAS减一
         * 所有子线程都执行完后（即state=0），会unpark()调用线程，
         * 然后主线程就会从await()函数返回，继续后续动作
         */

    }

}
