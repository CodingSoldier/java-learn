package com.example.bingfa02;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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




/*
    公平锁是按照线程请求顺序，来分配锁。
    非公平是指不完全按照请求循序，在一定情况下可以插队。

    非公平同样不提倡插队行为，但允许再合适的时机插队

    一种不公平现象举例，以ReentrantLock为例子：
        如果线程1持有锁，线程2、3在队列中等待。线程1释放锁的时候，线程4恰好去执行lock()。由于唤醒线程2需要时间，线程2还没有获取到锁，为了更加有效利用CPU，线程4将获取到锁。

    tryLock()不遵守设定的公平规则
    当有线程执行tryLock()的时候，一旦有线程释放了锁，那么这个正在tryLock的线程就能获取到锁，即使在他之前已经有其他正在等待的队列。

 */
class FairLock{
    public static void main(String[] args) {
        // 非公平锁，默认
        ReentrantLock reentrantLock = new ReentrantLock(false);

        // 公平锁
        reentrantLock = new ReentrantLock(true);

    }
}


/**
排他锁：又称为独占锁、独享说。拿到锁的线程可以读写数据、没拿到锁的线程不能读写数据。
共享锁：又称为读锁，获得共享锁之后，可以查看但无法修改、删除数据。其他线程此时也可以获取到共享锁，可以查看数据，但是无法修改、删除数据。

读写锁规则：
    1、多个线程只申请读锁，可以申请到。
    2、一个线程持有读锁，其他线程申请写锁，申请锁的线程会阻塞，直到读锁被释放。
    3、一个线程持有读锁，其他线程申请读锁、写锁，申请锁的线程会阻塞，直到写锁释放。
    总结：读锁可被多个线程同时拥有，写锁只能被一个线程同时拥有；读锁、写锁互斥不能同时出现。

公平锁不允许插队。非公平锁有两种策略

非公平锁读写锁插队策略
    策略1：线程1、线程2正在读取；线程3要写入，拿不到写锁，线程3进入等待队列；此时线程4要获取读锁，线程4能获取到读锁。
           这种策略效率高，但是容易造成写锁等待时间长，造成写线程饥饿。
    策略2：不允许插队，避免饥饿。ReentrantReadWriteLock默认使用这种策略。

非公平锁插队策略：
    写锁可以随时插队
    读锁仅在等待队列头结点不是获取写锁线程的时候可以插队

ReentrantReadWriteLock源码
 公平锁：
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -2274990926593161451L;

        写操作是否阻塞的判断依据是队列中是否有线程
        final boolean writerShouldBlock() {
            return hasQueuedPredecessors();
        }

        写操作是否阻塞的判断依据是队列中是否有线程
        final boolean readerShouldBlock() {
            return hasQueuedPredecessors();
        }
    }

 非公平锁
     static final class NonfairSync extends Sync {
         private static final long serialVersionUID = -8159625535654395037L;

        不需要等待，可以插队
         final boolean writerShouldBlock() {
            return false; // writers can always barge
         }

         读锁判断队列中的头节点是否是排他锁
         final boolean readerShouldBlock() {
            return apparentlyFirstQueuedIsExclusive();
         }
     }

读锁调用栈
 ReentrantReadWriteLock.Sync#tryAcquireShared(int)
    readerShouldBlock()  在获取读锁之前检查是否应该排队



 */
class ReadWriteLock01{

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

     // 非公平读写锁，默认
    // private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void read(){
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"得到读锁");
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000) + 3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            System.out.println(Thread.currentThread().getName()+"释放读锁");

            /**
             * unlock()要放在finally第一句，保证锁能释放
             */
            readLock.unlock();

            /**
             * 打印语句放在后面可能导致锁释放，其他线程拿到锁后，本线程才打印出释放锁
             */
            //System.out.println(Thread.currentThread().getName()+"释放读锁");
        }
    }

    private static void write(){
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"得到写锁");
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000) + 3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            System.out.println(Thread.currentThread().getName()+"释放写锁");

            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws Exception{
        new Thread(() -> read(), "线程1").start();
        new Thread(() -> read(), "线程2").start();
        TimeUnit.MILLISECONDS.sleep(100);
        new Thread(() -> write(), "线程3").start();
        new Thread(() -> write(), "线程4").start();

    }

}

/**
 *
 */
class ReadWriteLock02{

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void read(){
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"得到读锁");
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            System.out.println(Thread.currentThread().getName()+"释放读锁");
            readLock.unlock();
        }
    }

    private static void write(){
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"得到写锁");
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            System.out.println(Thread.currentThread().getName()+"释放写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws Exception{
        /**
         * 非公平锁
         *      线程1持有读锁
         *      线程2能继续获取读锁
         *      线程3尝试获取写锁
         *      线程4尝试获取读锁，没成功。因为队列第一个线程要获取写锁。直到队列写线程结束，线程4才能获取读锁
         */
        new Thread(() -> read(), "线程1").start();
        TimeUnit.MILLISECONDS.sleep(10);
        new Thread(() -> read(), "线程2").start();
        TimeUnit.MILLISECONDS.sleep(10);
        new Thread(() -> write(), "线程3").start();
        new Thread(() -> read(), "线程4").start();
    }

}


class Nonfair{

     //private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    // 非公平锁演示，读锁插队演示
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void read(){
        System.out.println(Thread.currentThread().getName()+"开始尝试获取读锁");
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获取到读锁");
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally{
            System.out.println(Thread.currentThread().getName()+"释放读锁");
            readLock.unlock();
        }
    }

    private static void write(){
        System.out.println(Thread.currentThread().getName()+"开始尝试获取写锁");
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获取到写锁");
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally{
            System.out.println(Thread.currentThread().getName()+"释放写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(() -> write(), "wwww线程1，").start();
        new Thread(() -> read(), "rrrr线程2，").start();
        new Thread(() -> read(), "rrrr线程3，").start();
        new Thread(() -> read(), "rrrr线程4，").start();
        new Thread(() -> read(), "rrrr线程5，").start();
        new Thread(() -> write(), "wwww线程6，").start();
        new Thread(() -> read(), "rrrr线程7，").start();
        new Thread(() -> read(), "rrrr线程8，").start();

        /**
         reentrantReadWriteLock = new ReentrantReadWriteLock(true);
         公平锁，打印 开始尝试获取读锁 的线程与 获取到读锁 的线程顺序一样
         证明所有线程在队列中排队了

         reentrantReadWriteLock = new ReentrantReadWriteLock(false);
         非公平锁，打印 开始尝试获取读锁 的线程与 获取到读锁 的线程顺序不一样，
         有些子线打印了 开始尝试获取读锁 后马上打印 获取到读锁
         输出流程如下：
            线程1获取写锁
            其他线程以及子线程开始尝试获取读锁，并排队
            线程1释放写锁
            会出现子线程开始获取读锁后，马上能获取到读锁，此线程插队了。比那些已经排队的读线程更早拿到读锁

         */
        new Thread(() -> {
            Thread thread[] = new Thread[1000];
            for (int i = 0; i < 1000; i++) {
                thread[i] = new Thread(() -> read(), "zzzz子线程创建的线程" + i);
            }
            for (int i = 0; i < 1000; i++) {
                thread[i].start();
            }
        }).start();
    }

}




/**
 锁可以降级
    同步代码中，当写入操作完成，还有读取操作，此时可以将锁降级为读锁。
 锁不支持升级
    升级会造成死锁，若A、B线程同时持有读锁，又同时想升级写锁，则A、B都希望对方释放读锁，造成死锁。
 */
class LockUpgradingDowngrading{
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void writeDowngrading(){
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获取到写锁");
            TimeUnit.SECONDS.sleep(1);

            System.out.println(Thread.currentThread().getName()+"尝试获取读锁，锁可以降级");
            readLock.lock();
            System.out.println(Thread.currentThread().getName()+"锁降级成功");
            readLock.unlock();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            writeLock.unlock();
        }
    }

    private static void readLockUpgrading(){
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获取到读锁");
            TimeUnit.SECONDS.sleep(1);

            System.out.println(Thread.currentThread().getName()+"尝试获取写锁。但是锁无法升级，线程阻塞");
            writeLock.lock();
            System.out.println(Thread.currentThread().getName()+"锁升级成功，此代码不会执行");
            writeLock.unlock();

        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            readLock.unlock();
        }
    }

    public static void main(String[] args) {

        // 锁降级
        //new Thread(() -> writeDowngrading()).start();

        // 锁升级
        new Thread(() -> readLockUpgrading()).start();
    }
}


/**
 atmoic包下的类基本上都是自旋锁的实现
 AtomicInteger自旋锁的实现原理是CAS
 AtomicInteger中调用unsafe进行自增操作的源码中
    有一个do-while循环，循环是自旋操作。如果修改过程中遇到其他线程竞争导致没修改成功，就继续循环，直到修改成功。

 public final int incrementAndGet() {
    return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
 }

 public final int getAndAddInt(Object var1, long var2, int var4) {
     int var5;
     do {
        var5 = this.getIntVolatile(var1, var2);
     } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
     return var5;
 }

 自旋锁使用场景：
    自旋锁一般用于多核服务器，在并发度不是特别高的情况下比阻塞锁效率高。
    自旋锁适用于临界区比较小的情况，如果临界区很大（线程拿到锁后很久才释放），那也是不适合的。

 JVM对锁的优化：
    自旋锁锁自适应，尝试N次失败后转为阻塞锁，N也可以自适应调节大小。
    锁消除，虚拟机判断不需要加锁，把锁消除。
    锁粗化，一系列的加锁解锁是对同一个资源反复加锁，把锁范围加大

 */
class SpinLock{
    private AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock(){
        Thread currentThread = Thread.currentThread();
        // 在一个原子操作内比较旧值是否为null，如果是则设置新值为currentThread
        while (!sign.compareAndSet(null, currentThread)){
            System.out.println(currentThread.getName()+"  compareAndSet失败，自旋，再次尝试");
        }
    }

    public void unlock(){
        Thread currentThread = Thread.currentThread();
        sign.compareAndSet(currentThread, null);
    }

    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName()+"尝试获取锁");
            spinLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到自旋锁");
                TimeUnit.MILLISECONDS.sleep(1);
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally {
                spinLock.unlock();
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}








