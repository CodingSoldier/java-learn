package com.example.bingfa02;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class C_Lock {

}

/*
Lock并不是要用来替代synchronized的，而是当使用synchronized不合适或者不满足要求的时候，Lock可以提供更多高级功能

通常情况下Local只允许一个线程访问这个共享资源，不会有的时候一些特殊实现也可允许并发访问，比如ReadWriteLock中的ReadLock。

synchronized：
    效率低：锁的释放情况少、不能设置锁的超时时间、不能中断正在试图获得锁的线程。
    不够灵活：加锁、释放锁的时机单一，每个锁仅有单一的条件（某个对象），可能是不够的。
    无法知道是否成功获取到锁。


 */


class LockMustUnlock {
    private static Lock lock = new ReentrantLock();

    /**
     * lock()方法：
     * 获取锁。如果锁被其他线程获取，则进行等待。
     * Lock锁不会像synchronized一样在异常时自动释放锁。
     * 最佳实践是在finally中释放锁，以保证发生异常时锁一定被释放。
     * <p>
     * lock()方法不能被中断，则会带来很大的隐患。一旦陷入死锁，lock()就会陷入永久等待。
     */
    public static void main(String[] args) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }
}




/**
 * tryLock()方法
 * 尝试获取锁，如果当前锁没有被其他线程占用，则返回true，获取成功。否真返回false，获取锁失败
 * 相比于lock()方法，可以根据能否获取到锁来决定后续程序的行为
 * 该方法会立即返回，即使在拿不到锁时也不会一直等待
 * <p>
 * tryLock(long time, TimeUnit unit) 超时就放弃。可以避免死锁
 */
class TryLockTimeOut implements Runnable {

    int flag = 1;
    static Lock lock1 = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        TryLockTimeOut r1 = new TryLockTimeOut();
        TryLockTimeOut r2 = new TryLockTimeOut();
        r1.flag = 1;
        r2.flag =0;
        new Thread(r1).start();
        new Thread(r2).start();

    }

    /**
     * 使用tryLock(long time, TimeUnit unit)避免死锁
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (flag == 1) {
                try {

                    if (lock1.tryLock(100, TimeUnit.MILLISECONDS)) {

                        try {
                            System.out.println(Thread.currentThread().getName() + "获取到锁1");
                            TimeUnit.MILLISECONDS.sleep(new Random(1000).nextInt());

                            if (lock2.tryLock(800, TimeUnit.MILLISECONDS)) {

                                try {
                                    System.out.println(Thread.currentThread().getName() + "获取到两把锁");
                                } finally {
                                    lock2.unlock();
                                }

                            } else {
                                System.out.println(Thread.currentThread().getName() + "获取锁2失败");
                            }

                        } finally {
                            lock1.unlock();
                        }

                    } else {
                        System.out.println(Thread.currentThread().getName() + "获取锁1失败");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {

                    if (lock2.tryLock(100, TimeUnit.MILLISECONDS)) {

                        try {
                            System.out.println(Thread.currentThread().getName() + "获取到锁2");
                            TimeUnit.MILLISECONDS.sleep(new Random(1000).nextInt());

                            if (lock1.tryLock(800, TimeUnit.MILLISECONDS)) {

                                try {
                                    System.out.println(Thread.currentThread().getName() + "获取到两把锁");
                                } finally {
                                    lock1.unlock();
                                }

                            } else {
                                System.out.println(Thread.currentThread().getName() + "获取锁1失败");
                            }

                        } finally {
                            lock2.unlock();
                        }

                    } else {
                        System.out.println(Thread.currentThread().getName() + "获取锁2失败");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}




class LockInterruptibly implements Runnable{

    Lock lock = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        LockInterruptibly lockInterruptibly = new LockInterruptibly();
        Thread thread0 = new Thread(lockInterruptibly);
        Thread thread1 = new Thread(lockInterruptibly);
        thread0.start();
        thread1.start();

        TimeUnit.SECONDS.sleep(2);

        //thread0.interrupt();
        thread1.interrupt();

    }

    /**
     * 持有锁期间线程可以被中断
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "尝试获取锁");
        try {

           lock.lockInterruptibly();
           try {
               System.out.println(threadName + "获取到锁");
               TimeUnit.SECONDS.sleep(5);
           }catch (InterruptedException e){
               System.out.println(threadName + "睡眠期间被中断");
           }finally {
               lock.unlock();
           }

        }catch (InterruptedException e){
            System.out.println(threadName + "持有锁期间被中断");
        }
    }
}


/*
 锁的分类
      ReentrantLock既是互斥锁、也是可重入锁

 乐观锁：
      如果数据和我一开始拿到的不一样了，说明其他人在这段时间内修改过数据，那我就不能继续刚才的更新数据过程了，我会选择放弃、报错、重试等策略
      乐观锁一般使用CAS算法来实现。核心实现思想是在一个原子操作内对比并交换数据

 临界区：只有一个线程能执行的的代码片段叫做临界区。当有一个线程进入临界区代码，其他线程只能等待。
 悲观锁：
    适合并发写入多的情况，适用于临界区持锁时间比较长的情况，悲观锁可以避免大量无用自旋等的消耗。典型使用场景：
        临界区有IO操作
        临界区代码复杂或者循环量大
        临界区竞争激烈
 乐观锁：
    适合并发写入少，大部分是读取的场景，不加锁能让读取性能大幅度提高


*/

/**
 * 可重入锁重入性质演示
 */
class ReentrantLockRecursion{

    private static ReentrantLock lock = new ReentrantLock();

    private static void accessResource(){
        lock.lock();
        try {
            if (lock.getHoldCount() < 5){
                System.out.println("递归前重入次数：" + lock.getHoldCount());
                accessResource();
                System.out.println("递归后重入次数：" + lock.getHoldCount());
            }
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        accessResource();
    }
}

























