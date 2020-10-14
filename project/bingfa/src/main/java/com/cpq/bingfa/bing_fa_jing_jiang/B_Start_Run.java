package com.cpq.bingfa.bing_fa_jing_jiang;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class B_Start_Run {

    @Test
    public void startrun() throws Exception{
        Runnable runnable = () -> {
            System.out.println("运行了");
            System.out.println(Thread.currentThread().getState());
            System.out.println(Thread.currentThread().getName());
        };

        // main线程直接调用Runnable.run()
        // 这种方法是错误的，没有意义。如果这样用，那还不如直接把run()方法代码写在本方法中
        // 我们创建Runnable的目的就是为了用多线程并行执行多个任务
        runnable.run();

        // 新建线程去调用Runnable.run()
        Thread thread = new Thread(runnable);
        thread.start();

        /**
         * 线程不能连续调用两次start()，因为线程start后就变成了其他状态，在start()源码中有if判断，若线程状态不是未启动状态，就抛出异常
         * start()其实使用的是native虚拟机本地方法
         */
        //thread.start();

        TimeUnit.SECONDS.sleep(5);
    }

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(()-> {
            while (true){
                System.out.println("Thread.yield()前"+Thread.currentThread().getState());
                Thread.yield();
                System.out.println("Thread.yield()后"+Thread.currentThread().getState());
            }
        });
        thread.start();
        Thread.sleep(2000);
        System.out.println("最后"+thread.getState());
    }

}







/**
 * 线程状态。 线程可以处于以下状态之一：:
 * <ul>
 * <li>{@link #NEW}<br>
 *     尚未启动的线程处于此状态。
 *     </li>
 * <li>{@link #RUNNABLE}<br>
 *     在Java虚拟机中执行的线程处于这种状态。
 *     </li>
 * <li>{@link #BLOCKED}<br>
 *     等待监视器锁定而被阻塞的线程处于此状态。
 *     </li>
 * <li>{@link #WAITING}<br>
 *     无限期地等待另一个线程执行特定操作的线程处于此状态。
 *     </li>
 * <li>{@link #TIMED_WAITING}<br>
 *     等待另一个线程执行某个操作直到指定等待时间的线程处于这种状态。
 *     </li>
 * <li>{@link #TERMINATED}<br>
 *     退出的线程处于此状态。
 *     </li>
 * </ul>
 *
 * <p>
 * 在给定的时间点，线程只能处于一种状态。这些状态是虚拟机状态，不反映任何操作系统线程状态。
 *
 * @since   1.5
 * @see #getState
 */
public enum State {
    /**
     * 尚未启动的线程的线程状态
     */
    NEW,

    /**
     * 可运行线程的线程状态。处于可运行状态的线程正在Java虚拟机中执行，但是它可能正在等待来自操作系统（例如处理器）的其他资源。
     * 我的理解：
     *      处于RUNNABLE状态的线程可能获得了CPU资源，正在被运行，也可能在等待CPU资源，没被CPU运行。
     *      但是JVM将线程在硬件层面的两种状态归为了一种
     */
    RUNNABLE,

    /**
     * 等待监视器锁而被阻塞的线程的线程状态。
     * 一个处于阻塞状态的线程正在等待一个监视器锁进入一个同步的块/方法，或者在调用Object.wait之后重新进入一个同步的块/方法。
     */
    BLOCKED,

    /**
     * 等待线程的线程状态。线程处于等待状态是由于调用了以下方法之一:
     * Object.wait()，注意：此方法没有超时参数
     * Thread.join()，注意：此方法没有超时参数
     * LockSupport.park()
     * 处于等待状态的线程正在等待另一个线程执行特定的操作。
     * 例如，在一个对象上调用object.wait()的线程正在等待另一个线程调用该对象上的object.notify()或object.notifyAll()。调用thread.join()的线程正在等待指定的线程终止。
     */
    WAITING,

    /**
     * 具有指定等待时间的等待线程的线程状态。
     * 一个线程处于定时等待状态，这是由于调用了以下方法中的一种，具有指定的正等待时间:
     * Thread.sleep(long timeout)，注意：此方法有超时参数
     * Object.wait(long timeout)，注意：此方法有超时参数
     * Thread.join(long millis)，注意：此方法有超时参数
     * LockSupport.parkNanos()
     * LockSupport.parkUntil()
     */
    TIMED_WAITING,

    /**
     * 终止线程的线程状态。线程已完成执行。
     */
    TERMINATED;

}