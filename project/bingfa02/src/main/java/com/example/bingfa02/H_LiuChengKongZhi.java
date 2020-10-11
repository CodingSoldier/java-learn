package com.example.bingfa02;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class H_LiuChengKongZhi {
}

/*
控制并发流程的工具类
Semaphore
    信号量，可以通过控制“许可证”的数量，来保证线程之间的配合
    线程只有在拿到“许可证”后才能继续运行，相比于其他的同步器，更灵活
CyclicBarrier
    线程会等待，直到足够多线程达到了事先规定的数目，一旦达到触发条件，就可以进行下一步的动作
    适用于线程之间相互等待处理结果就绪的场景
Phaser
    和CyclicBarrier类似，但是计数可变，Java 7加入
CountDownLatch
    和CyclicBarrier类似，数量递减到0时触发动作
    不可重复使用
Exchanger
    让两个线程在合适时交换对象
    使用场景：当两个线程工作在同一个类的不同实例上时，用于交换数据
Condition
    可以控制线程的等待、唤醒。是Object.wait()的升级版

 */


/**
 CountDownLatch
    倒数门栓
    流程：倒数结束前一直处于等待状态，知道倒计时结束了，此线程才继续工作

 CountDownLatch(int count)： 只有一个构造函数，参数count为需要倒数的数值
 await()：调用await()方法的线程会被挂起，此线程会等待直到count值为0才继续执行
 countDown(): 将count值减一，直到为0时，等待的线程才会被唤起
 */

/**
 * 用法一：一个线程等待多个线程都执行完毕，再继续自己的工作
 */
class CountDownLatchDemo01{

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i+1;
            executorService.submit(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5000));
                    System.out.println("No."+no+"执行完毕");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally{
                    // 每个线程在finally中执行countDown()，计数器减一
                    latch.countDown();
                }
            });
        }

        System.out.println("等待5个人检查完毕。。。。");
        // 当前线程执行await()等待，计数器为0
        latch.await();
        System.out.println("所有人都完成了工作，进入下一个环节");
    }

}



/**
 CyclicBarrier循环栅栏
 CyclicBarrier循环栅栏和CountDownLatch很类似，都能阻塞一组线程
 当有大量线程相互配合，分别计算不同任务，并且需要最后统一汇总的时候，我们可以使用CyclicBarrier。CyclicBarrier可以构造一个集合点，当某一个线程执行完毕，他就会到集合点等待，直到所有线程都到了集合点，那么该栅栏就被撤销，所有线程统一出发，执行剩下的任务。

 举例：咱们3个人明天中午在学校碰面，都到齐后，一起讨论下个学期的计划

 CyclicBarrier和CountDownLatch的区别
    作用不同：CyclicBarrier是要等待固定数量的线程都到达了栅栏位置才能继续执行，而CountDownLatch只需要等待数字到0。也就是说CountDownLatch用于事件，而CyclicBarrier用于线程
    可重用性不同：CountDownLatch在倒数到0并触发门栓打开后就不能再次使用了，除非新建实例。而CyclicBarrier可以重复使用
 */

class CyclicBarrierDemo{

    public static void main(String[] args) {
        /**
         * 有五个线程准备完毕（线程执行cyclicBarrier.await()便是准备完毕），就执行本构造函数的run()方法
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("已经有5个线程都执行了cyclicBarrier.await()，本方法体执行完毕，便会将这5个线程设置为Runnable状态");
        });

        for (int i = 0; i < 10; i++) {
            new Thread(new Task(cyclicBarrier)).start();
        }
    }


    static class Task implements Runnable{
        private CyclicBarrier cyclicBarrier;

        public Task(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(5)+1);
                System.out.println(Thread.currentThread().getName()+"准备完毕，等待其他线程准备完毕");
                // 等待其他线程准备完毕
                cyclicBarrier.await();

                // cyclicBarrier构造函数的run()方法执行完毕，本线程从阻塞状态变为Runnable状态，继续执行下面的代码
                System.out.println(Thread.currentThread().getName()+"从阻塞状态变为Runnable状态，执行接下来的任务");
            }catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}


/**
 Semaphore信号量
    可用来限制或者管理数量资源的使用情况

 信号量的作用是维护一个“许可证”的计数，线程可以“获取”许可证，那信号量剩余的许可证就减一，线程也可以“释放”一个许可证，那信号量剩余的许可证就加一，当信号量所拥有的许可证数量为0，那么下一个还想要获取许可证的线程就需要等待，直到有另外的线程释放了许可证

 信号量使用流程：
    1、初始化Semaphore并指定许可证数量
    2、在需要被限制的代码前加上acquire()或者acquireUninterruptibly()方法
    3、在任务执行结束后，调用release()来释放许可证

 重要方法介绍
    new Semaphore(int permits, boolean fair)，可设置是否使用公平策略，如果传入true，那么Semaphore会把之前等待的线程放到FIFO的队列里，以便当有了新的许可证可以分发给之前等了很长时间的线程。
    tryAcquire()，看看线程有没有空闲的许可证，如果有就获取。如果没有，线程可以不必进入阻塞，可以去做其他事，过一会儿再来查看许可证的空闲情况。
    tryAcquire(timeout)，和tryAcquire()一样，但是多了一个超时时间，比如“在3秒内获取不到许可证，我就去做别的事”
    acquire()，获取许可证，获取不到就阻塞，可以被中断
    acquireUninterruptibly()，和acquire()一样，但是不能响应中断
    release()，归还许可证

    acquire(int n)，一次可以拿多个许可证
    release(int n),一次释放多个许可证。如无特殊需求，获取和释放的数量要一致

 并不要求获取许可证的线程释放许可证，A线程获取一个许可证，B线程释放许可证也是可以的

 */
class SemaphoreDemo{
    static Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"获取许可证");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5)+1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName()+"释放许可证");
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()){};
    }
}


/**
 Condition的作用
    当线程1需要等待某个条件的时候，它就去执行condition.await()方法，一旦执行了await()方法，线程就会进入阻塞状态
    然后通常会有另外一个线程，假设是线程2，去执行对应的条件。直到这个条件达成的时候，线程2就会去执行condition.signal()方法，这是JVM就会从被阻塞的线程中找，找到那些等待该condition的线程，线程1收到可执行信号的时候，它的线程状态就会变成Runnable可执行状态。

 signalAll()会唤起所有正在等待的线程
 signal()是公平的，只会唤起那个等待时间最长的线程
 */

/**
 * condition基本用法
 */
class ConditionDemo1{
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"条件不满足，等待");
                condition.await();
                System.out.println(Thread.currentThread().getName()+"条件满足了，继续执行");
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"准备完成，唤醒其他线程");
                condition.signal();
            }finally{
                lock.unlock();
            }
        }).start();

    }
}


/**
 * 如果说Lock用来替代synchronized，那么Condition就是用来替代Object.wait()/notify()的，所以在用法和性质上，几乎一样
 * await()方法会自动释放持有的Lock锁，和Object.wait()一样，不需要自己动手先释放锁
 * 调用await()的时候，必须先持有锁，否则会抛出异常，和Object.wait()一样
 *
 * Condition实现生产者消费者模式
 */

class Condition02{

    private int queueSize = 100;
    private PriorityQueue<Integer> queue = new PriorityQueue<>();
    private Lock lock = new ReentrantLock();
    /**
     * ReentrantLock可以创建多个Condition条件
     */
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
        Condition02 condition02 = new Condition02();
        Comsumer comsumer = condition02.new Comsumer();
        Producer producer = condition02.new Producer();

        producer.start();
        comsumer.start();
    }

    class Comsumer extends Thread{
        @Override
        public void run() {
            comsumer();
        }

        private void comsumer(){
            while (true){
                lock.lock();
                try {
                    if (queue.size() ==0){
                        System.out.println("队列空，等待生产数据");
                        notEmpty.await();
                    }
                    Integer e = queue.poll();
                    System.out.println("消费数据 "+e+" ，唤醒生产者");
                    notFull.signalAll();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    lock.unlock();
                }
            }
        }
    }

    class Producer extends Thread{
        @Override
        public void run() {
            producer();
        }

        private void producer(){
            while (true){
                lock.lock();
                try {
                    if (queue.size() == queueSize){
                        System.out.println("队列满了，等待消费数据");
                        notFull.await();
                    }
                    int i = new Random().nextInt(100);
                    queue.add(i);
                    System.out.println("生产数据 "+i+" ，唤醒消费者");
                    notEmpty.signalAll();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    lock.unlock();
                }
            }
        }
    }

}

























