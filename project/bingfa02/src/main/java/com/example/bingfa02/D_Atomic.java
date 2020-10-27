package com.example.bingfa02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class D_Atomic {
}
/**
 AtomicInteger常用方法
    get()    获取当前的值
    getAndSet(int newValue)   获取当前值，并设置新的值
    getAndIncrement()   获取当前值，并自增
    getAndDecrement()  获取当前的值，并自减
    getAndAdd(int delta)  获取当前值，并加上预期的值
    compareAndSet(int expect, int update)  如果当前的数字等于预期值expect，则以原子方式将该值设置为输入值update
 */
class AtomicInteger01 implements Runnable{
    private static AtomicInteger atomicInteger = new AtomicInteger();
    private static int basiceCount = 0;

    /**
     * 原子类递增，每次增加10
     */
    private void incrementAtomic(){
        atomicInteger.getAndAdd(1);
    }

    /**
     * ++ 方式自增
     */
    private void incrementBasic(){
        basiceCount++;
    }

    @Override
    public void run() {
        for (int i=0; i<100000; i++){
            incrementAtomic();
            incrementBasic();
        }
    }

    public static void main(String[] args) throws Exception {
        AtomicInteger01 runnable = new AtomicInteger01();
        for (int i = 0; i < 5; i++) {
            new Thread(runnable).start();
        }
        TimeUnit.SECONDS.sleep(10);
        System.out.println("原子类结果："+atomicInteger.get());
        System.out.println("普通变量结果："+basiceCount);

    }
}



/**
 AtomicIntegerFieldUpdater对普通变量进行升级
    使用场景：偶尔需要一个原子get-set操作
 */
class Candidate{
    /**
     * 属性不能是private、不能是static
     */
    volatile int score;
}
class AtomicIntegerFieldUpdaterDemo implements Runnable{
    static Candidate safe;
    static Candidate nosafe;
    /**
     * 对Candidate的score属性进行包装，使score属性具备原子性
     */
    public static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            nosafe.score++;
            scoreUpdater.getAndIncrement(safe);
        }
    }

    public static void main(String[] args) throws Exception{
        safe = new Candidate();
        nosafe = new Candidate();
        AtomicIntegerFieldUpdaterDemo runnable = new AtomicIntegerFieldUpdaterDemo();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通变量："+nosafe.score);
        System.out.println("升级类结果："+safe.score);
    }
}


/**
 累加器Adder。jdk8新增。比Atomic类更加高效
 Atomic每次进行加法都要flush和refresh。CPU修改Atomic后，马上把Atomic刷新（flush）到主内存中，然后把主内存中的值refresh到各个线程中

 使用LongAdder，每个线程都会有自己的一个计数器，每个线程对自己线程内的计数器计数，不受其他线程计数器干扰。
 如 D_LongAdder的改进和原理.jpg 图，线程1的计数器ctr'数值是1，线程2的计数器ctr''数值是3，他们之间不存在竞争关系。
 LongAdder引入了分段累加的概念，内部有一个base变量和Cell[]数组共同参数计数
     base变量：竞争不激烈的时候，直接累加到该变量上
     Cell[]数组：竞争激烈，各个线程分散累加到自己的槽Cell[i]中
 Adder的sum()方法源码分析
     public long sum(){
        Cell[] as = cells; Cell a;
        long sum = base;
        // 并发量小的时候，as=null，直接返回base。
        // 并发量大的时候，base加上Cell元素的值
        // 可能存在返回sum的时候Cell数组被修改的情况
        if (as != null){
            for (int i=0; i<as.length; ++i){
                if(a=as[i] != null){
                    sum += a.value;
                }
            }
        }
        return sum;
     }

 LongAdder适用于统计求和
 */
class LongAdder1 implements Runnable{

    private LongAdder counter;

    public LongAdder1(LongAdder counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            counter.increment();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < 100; i++) {
            executorService.submit(new LongAdder1(longAdder));
        }
        // 等待线程停止
        executorService.shutdown();
        while (!executorService.isTerminated()){}

        System.out.println("数量"+longAdder.sum());

    }
}


/**
 Accumulator和Adder非常相似，Accumulator就是一个更通用版本的Adder

 适用场景：大数据并行计算，
 */
class LongAccumulator01{
    public static void main(String[] args) {
        // x的初值是1，传入的参数是y，执行 x+y
        //LongAccumulator accumulator = new LongAccumulator((x, y) -> x + y, 1);

        // x的初值是1，传入参数是y，返回x、y的最大值
        LongAccumulator accumulator = new LongAccumulator((x, y) -> Math.max(x, y), 1);

        // 使用多线程执行accumulator计算
        ExecutorService executor = Executors.newFixedThreadPool(10);
        IntStream.range(1, 10)
                .forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        executor.shutdown();
        while (!executor.isTerminated()){}

        System.out.println(accumulator.getThenReset());
    }
}







