package com.example.bingfa02;

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

 state: state的具体含义，会根据具体实现类的不同而不同，比如在Semaphore中，state表示剩余的许可证数量。而在CountDownLatch中，他表示还需要倒数的数量。在ReentrantLock中表示锁的重入计数，沙特为0表示锁不被任何线程占有
 state是volatile修饰的，会被并发地修改，所以所有修改state的方法都要保证线程安全，比如getState、setState以及compareAndSetState操作来读取和更新这个状态，这些方法都依赖atomic包

 FIFO队列：这个队列用来存放“等待的线程”，AQS就是“排队管理器”，当多个线程争用同一把锁时，必须有排队机制将那些没能拿到的锁线程串联在一起，当锁释放时，锁管理器就会挑选一个合适的线程来占用这个刚刚释放的锁
 AQS会维护一个等待线程队列，把线程都放到这个队列里

 获取/释放的方法：由协作类自己去实现，并且含义各不相同
    获取方法：获取操作会依赖state变量，经常会阻塞（比如获取不到锁的时候）。在Semaphore中，获取是acquire方法，作用是获取一个许可证。在CountDownLatch中，获取就是await方法，作用是等待直到倒数结束
    释放方法：释放操作不会阻塞，在Semaphore中，释放就是release方法，作用是释放一个许可证。在CountDownLatch中，获取就是countDown方法，作用是倒数1个数。
 */
public class I_AQS {

    public static void main(String[] args) {

    }

}
