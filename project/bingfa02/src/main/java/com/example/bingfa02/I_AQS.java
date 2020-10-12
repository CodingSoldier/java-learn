package com.example.bingfa02;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 AQS全称AbstractQueuedSynchronizer
     new Semaphore();
     new CountDownLatch();
     new CyclicBarrier()
     内部有一个Sync内部类，Sync继承了AQS

 AQS是一个用于构建锁、同步器、协作工具类的工具类（框架）。有了AQS以后，更多的协作工具类都可以很方便被写出来。
 有了AQS，构建线程协作类就容易多了

 AQS核心的三大部分
    state
    控制线程抢锁和配合的FIFO队列
    期望协作工具类去实现的获取/释放等重要方法

 state: state的具体含义，会根据具体实现类的不同而不同，比如在Semaphore中，state表示剩余的许可证数量。而在CountDownLatch中，他表示还需要倒数的数量。在ReentrantLock中表示锁的重入计数，state为0表示锁不被任何线程占有
 state是volatile修饰的，会被并发地修改，所以所有修改state的方法都要保证线程安全，比如getState、setState以及compareAndSetState操作来读取和更新这个状态，这些方法都依赖atomic包

 FIFO队列：这个队列用来存放“等待的线程”，AQS就是“排队管理器”，当多个线程争用同一把锁时，必须有排队机制将那些没能拿到的锁线程串联在一起，当锁释放时，锁管理器就会挑选一个合适的线程来占用这个刚刚释放的锁
 AQS会维护一个等待线程队列，把线程都放到这个队列里

 获取/释放的方法：由协作类自己去实现，并且含义各不相同
    获取方法：获取操作会依赖state变量，经常会阻塞（比如获取不到锁的时候）。在Semaphore中，获取是acquire方法，作用是获取一个许可证。在CountDownLatch中，获取就是await方法，作用是等待直到倒数结束
    释放方法：释放操作不会阻塞，在Semaphore中，释放就是release方法，作用是释放一个许可证。在CountDownLatch中，获取就是countDown方法，作用是倒数1个数。

 AQS的用法
    1、写一个类，想好协作逻辑，实现获取/释放方法
    2、内部写一个Sync类继承AbstractQueuedSynchronizer
    3、独占锁重写tryAcquire/tryRelease或tryAcquireShared(int acquires)，
       共享锁重写tryReleaseShared(int release)等方法，
       在之前写的获取/释放方法中调用AQS的acquire/release或者shared方法

 AQS在CountDownLatch中的应用
    构造函数
        创建 this.sync = new Sync(count)
            将AQS的state设置为count
    getCount方法
        返回Sync的state
    countDown方法
        Sync#tryReleaseShared(int)使用CAS自旋，让state减一。如果state由1减到0成功，则闸门放开
        AbstractQueuedSynchronizer#doReleaseShared()将之前等待的线程全部唤醒
    await方法
        如果state==0，本方法直接运行完毕，不阻塞。如果state != 0，将线程放入队列中doAcquireSharedInterruptibly(int arg)。AbstractQueuedSynchronizer#parkAndCheckInterrupt()此方法让线程挂起

 AQS在CountDownLatch中的应用总结
    1、调用CountDownLatch的await方法，会尝试获取“共享锁”，不过一开始是获取不到锁的，于是线程被阻塞
    2、共享锁的获取条件是锁计数器（state）的值为0
    3、锁计数器的初始值为count，而当一个线程调用该CountDownLatch对象的countDown()方法时，才将锁计数器减一
    4、count个线程调用countDown()之后，锁计数器才为0，而前面提到的等待获取共享锁的线程才能继续执行。

 AQS在Semaphore中的应用
    在Semaphore中，state表示许可证的剩余数量
    tryAcquire方法，判断nonfairTryAcquireShared返回结果，大于等于0的话，代表成功
    在nonfairTryAcquireShared方法中，检查许可证数量够不够这次需要的，用减法来计算，如果不够，返回负数，表示失败。如果够了，就用自旋加compareAndSetState来改变state状态，直到改变成功就返回正数，或者是期间被其他线程修改了导致剩余数量不够，那也返回负数代表获取失败

 AQS在ReentrantLock中的应用
    unlock释放锁方法
        tryRelease(int releases)使用state减去release（值为1），如果结果为0，则表示线程释放锁完毕（有时候是重入锁，需要unlock多次）
        unparkSuccessor(h)唤醒正在等待的线程
    lock加锁方法
        compareAndSetState(0, 1)，使用CAS，如果原来的值是0，并且能更新为1，说明获取到锁；
        线程获取到锁将当前线程设置为获取锁的线程
        线程未获取到锁，tryAcquire(arg)分成公平与非公平实现，主要是设置state值，并返回是否获取锁成功，如果获取不成功，则将线程入队并等待

 AQS在ReentrantLock中的应用分析
    tryRelease：由于是可重入锁，所以用state代表重入次数，每次释放锁，先判断是不是当前持有锁的线程释放的，如果不是就抛异常，如果是，重入次数减一，如果减到0，说明完全释放了锁，于是free设置为true,state设置为0。


 */
public class I_AQS {}

/**
 * 使用AQS自定义一个线程协作器
 */
class OneShotLatch{
    private final Sync sync = new Sync();

    public void signal(){
        sync.releaseShared(0);
    }
    public void await(){
        sync.acquireShared(0);
    }

    private class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected int tryAcquireShared(int arg) {
            return (getState() == 1) ? 1 : -1;
        }
        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;
        }
    }

    public static void main(String[] args) throws Exception{
        OneShotLatch oneShotLatch = new OneShotLatch();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"尝试获取latch，获取失败就等待");
                oneShotLatch.await();
                System.out.println(Thread.currentThread().getName()+"继续运行");
            }).start();
        }

        TimeUnit.SECONDS.sleep(1);
        System.out.println("闸门开放");
        oneShotLatch.signal();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("闸门只能使用一次");
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"尝试获取latch，闸门使用过了，不再等待");
            oneShotLatch.await();
            System.out.println(Thread.currentThread().getName()+"继续运行");
        }).start();
    }

}














