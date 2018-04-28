package com.thinkinginjava.q_concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Q_Concurrent {

}




// P654
class LiftOff implements Runnable {
    protected int countDown = 10; // Default
    private static int taskCount = 0;
    private final int id = taskCount++;
    public LiftOff() {}
    public LiftOff(int countDown) {
        this.countDown = countDown;
    }
    public String status() {
        return "#" + id + "(" +
                (countDown > 0 ? countDown : "Liftoff!") + "), ";
    }
    public void run() {
        while(countDown-- > 0) {
            System.out.print("\n**"+Thread.currentThread().getName()+"**"+status());
            Thread.yield();
        }
    }
}

class MainThread {
    public static void main(String[] args) {
        Thread thread = new Thread(new LiftOff());
        thread.start();
        /**
         * thread.start()调用new LiftOff()的run()方法由其他线程去执行。
         * 本线程执行thread.start()后马上就返回，本线程不执行new LiftOff()的run()方法，继续下边的输出代码，下边的输出代码极有可能优先执行
         */
        System.out.println("本代码极有可能优先执行");
    }
}

class MainThread2 {
    public static void main(String[] args) {
        for(int i=0; i<5; i++){
            new Thread(new LiftOff()).start();
            System.out.println("本代码极有可能优先执行" + i);
        }

    }
}





// P656
class Printer implements Runnable {
    private static int taskCount;
    private final int id = taskCount++;
    Printer() {
        System.out.println("Printer started, ID = " + id);
    }
    public void run() {
        System.out.println("Stage 1..., ID = " + id);
        Thread.yield();
        System.out.println("Stage 2..., ID = " + id);
        Thread.yield();
        System.out.println("Stage 3..., ID = " + id);
        Thread.yield();
        System.out.println("Printer ended, ID = " + id);
    }
}
class E01_Runnable {
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new Thread(new Printer()).start();
    }
}







interface Generator<T> { T next(); }
class Fibonacci implements Generator<Integer>, Runnable {
    private int count;
    private final int n;
    public Fibonacci(int n) { this.n = n; }
    public Integer next() { return fib(count++); }
    private int fib(int n) {
        if(n < 2) return 1;
        return fib(n-2) + fib(n-1);
    }
    public void run() {
        Integer[] sequence = new Integer[n];
        for(int i = 0; i < n; i++)
            sequence[i] = next();
        System.out.println("Seq. of " + n + ": " + Arrays.toString(sequence));
    }
}
class E02_Fibonacci {
    public static void main(String[] args) {
        for(int i = 1; i <= 5; i++)
            new Thread(new Fibonacci(i)).start();
    }
}




// P656
class CachedThreadPool{
    public static void main(String[] args) {
        //ExecutorService es = Executors.newCachedThreadPool();
        //newCachedThreadPool每次execute会创建一个线程，优先选这种

        //ExecutorService es = Executors.newFixedThreadPool(2);
        //newFixedThreadPool通过参数指定线程池中的线程

        ExecutorService es = Executors.newSingleThreadExecutor();

        for (int i=0; i<5; i++){
            es.execute(new LiftOff());
        }
        es.shutdown();
    }
}




/*
class TaskWithResult implements Callable<String>{
    private int id;
    public TaskWithResult(int id){
        this.id = id;
    }

    @Override
    public String call(){
        System.out.println("TaskWithResult.call方法执行，id="+id);
        return "TaskWithResult.call返回值：" + id;
    }
}
class CallableDemo{
    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<String>> lf = new ArrayList<Future<String>>();
        for (int i=0; i<10; i++){
            lf.add(es.submit(new TaskWithResult(i)));
        }

        for (Future<String> f: lf){
            try {
                System.out.println(f.get());
            }catch (Exception e){
                System.out.println(e);
            }finally {
                es.shutdown();
            }
        }
    }
}
*/

class TaskWithResult implements Callable<String> {
    private int id;
    public TaskWithResult(int id) {
        this.id = id;
    }
    public String call() {
        try {
            Thread.sleep(1000*id*2);
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println("TaskWithResult.call方法执行，id="+id);
        return "TaskWithResult.call返回值：" + id;
    }
}

class CallableDemo {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for(int i = 0; i < 10; i++)
            results.add(exec.submit(new TaskWithResult(i)));
        for(Future<String> fs : results)
            try {
                // fs.get()处于当前线程，但不会马上执行，若相应线程没有返回结果，则对应的get()会等待。例如：线程5的call没有返回结果，则results.get(5)的get()不会执行，但是只要“线程非数字5”的call返回结果，则results.get(非数字5)的get()就会执行
                System.out.println(fs.get());
            } catch(Exception e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
    }
}



