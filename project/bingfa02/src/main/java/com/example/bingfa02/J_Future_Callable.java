package com.example.bingfa02;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class J_Future_Callable {
}

/*
Runnable缺陷
    1、没有返回值
    2、不能抛出checked exception

Callable接口
    类似于Runnable，是被其他线程执行的任务
    使用者实现call方法
    有返回值

Callable与Future的关系
    使用Future.get方法获取Callable接口返回的结果
    通过Future.isDone()方法判断任务是否已经执行完毕
    取消任务
    限时获取任务结果
    在call()未执行完毕之前，调用get()的线程（假定是主线程）会被阻塞，直到call()方法返回结果后，future.get()才会得到结果，然后主线程才会切换到Runnable状态。
    所以Future是一个存储器，他存储了call()这个任务的结果，而这个任务的执行时间是无法提前确定的。

get()方法获取结果有5种情况
    1、任务正常完成，get()方法会立刻返回结果
    2、任务尚未完成（任务未开始或进行中），get()将阻塞并直到任务完成
    3、任务执行过程中抛出Exception，get()方法会抛出ExecutionException；不论call()执行时抛出什么异常，最后get()方法抛出的异常类型都是ExecutionException
    4、任务被取消，get方法会抛出CancellationException
    5、任务超时，get方法有一个重载方法，可以传入延迟时间，如果时间到了还没有获得结果，get方法会抛出TimeoutException

用法1：线程池submit方法返回Future
     执行submit方法时，线程池立刻返回一个空的Future容器，当线程的任务一旦执行完毕，线程池会把结果填入Future中

 */

class Future1{

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 线程池submit提交Callable实现类，返回Future
        Future<Integer> future = executorService.submit(new CallableTask());

        try {
            // future.get()阻塞到任务完成，并获取结果
            Integer r = future.get();

            System.out.println(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    static class CallableTask implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            TimeUnit.SECONDS.sleep(2);
            return new Random().nextInt();
        }
    }

}


/**
 * 批量提交任务，使用List接收返回结果
 */
class Future2{
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 创建ArrayList<Future>
        ArrayList<Future> futures = new ArrayList<>();

        // 使用for循环提交Callable，并将future添加到List中
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = executorService.submit(() -> {
                TimeUnit.SECONDS.sleep(1);
                return new Random().nextInt();
            });
            futures.add(future);
        }

        TimeUnit.SECONDS.sleep(2);

        // for循环获取Future值
        for (Future<Integer> future : futures) {
            try {
                Integer integer = future.get();
                System.out.println(integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }
}


/**
 * 演示get方法过程中抛出异常
 * Callable抛出异常时，主线程不会抛异常，直到执行future.get()的时候才会抛异常
 */
class Future3{
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Object> future = executorService.submit(() -> {
            throw new IllegalArgumentException("在Callable中抛出异常");
        });

        TimeUnit.SECONDS.sleep(1);

        System.out.println("任务是否全部运行完毕。"+future.isDone());

        try {
            Object o = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("抛出InterruptedException");
        } catch (ExecutionException e) {
            e.printStackTrace();
            // System.out.println(e.getCause().getMessage());
            System.out.println("抛出ExecutionException");
        }

        executorService.shutdown();
    }
}


/**
 超时演示

 cancel方法：取消任务
    1、如果这个任务还没开始执行，任务会被取消，未来也不会被执行，返回true
    2、如果任务已完成或已取消，cancel方法会执行失败，返回false
    3、如果任务已经执行，根据cancel(boolean mayInterruptIfRunning)参数取消任务，true取消，false不取消

 cancel(true)适用于：任务能处理interrupt的任务
 cancel(false)适用于：
    1、不能处理interrupt的任务，或者调用者不确定任务能否处理interrupt
    2、需要等待已经开始的任务执行完成。

 */
class FutureTimeout{
    private static final Ad DEFAULT_AD = new Ad("无网络时候的默认广告");
    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    static class Ad{
        String name;

        public Ad(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        Future<Ad> future = exec.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                System.out.println("线程sleep期间被中断了");
                return new Ad("线程被中断，返回默认广告");
            }
            return new Ad("某程的旅游广告");
        });

        Ad ad;
        try {
            ad = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            ad = new Ad("发生中断的默认广告");
        } catch (ExecutionException e) {
            ad = new Ad("异常时的默认广告");
        } catch (TimeoutException e) {
            ad = new Ad("超时的默认广告");
            System.out.println("超时，执行取消任务");
            boolean cancel = future.cancel(true);
            System.out.println("cancel的结果："+cancel);
        }

        System.out.println(ad);

        exec.shutdown();
    }

}


/**
 FutureTask是一种包装器，它同时实现了Callable、Runnable接口，可以把Callable转化为Future和Runnable。查看J02 FutureTask.jpg

 */
class FutureTask1{
    static ExecutorService service = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        Callable callable = () -> {
            System.out.println("子线程正在计算");
            TimeUnit.SECONDS.sleep(1);
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            return sum;
        };

        FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);

        service.submit(futureTask);
    }
}














