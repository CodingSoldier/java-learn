package com.thinkinginjava.q_concurrent;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.thinkinginjava.A_String.print;
import static com.thinkinginjava.A_String.printnb;

interface Generator<T> {
    T next();
}

public class Q_Concurrent {

}

// P654
class LiftOff implements Runnable {
    private static int taskCount = 0;
    private final int id = taskCount++;
    protected int countDown = 10; // Default

    public LiftOff() {
    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" +
                (countDown > 0 ? countDown : "Liftoff!") + "), ";
    }

    public void run() {
        while (countDown-- > 0) {
            System.out.print("\n**" + Thread.currentThread().getName() + "**" + status());
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
        for (int i = 0; i < 5; i++) {
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
        for (int i = 0; i < 5; i++)
            new Thread(new Printer()).start();
    }
}

class Fibonacci implements Generator<Integer>, Runnable {
    private final int n;
    private int count;

    public Fibonacci(int n) {
        this.n = n;
    }

    public Integer next() {
        return fib(count++);
    }

    private int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }

    public void run() {
        Integer[] sequence = new Integer[n];
        for (int i = 0; i < n; i++)
            sequence[i] = next();
        System.out.println("Seq. of " + n + ": " + Arrays.toString(sequence));
    }
}

class E02_Fibonacci {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++)
            new Thread(new Fibonacci(i)).start();
    }
}


// P656
class CachedThreadPool {
    public static void main(String[] args) {
        //ExecutorService es = Executors.newCachedThreadPool();
        //newCachedThreadPool每次execute会创建一个线程，优先选这种

        //ExecutorService es = Executors.newFixedThreadPool(2);
        //newFixedThreadPool通过参数指定线程池中的线程

        ExecutorService es = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
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
            Thread.sleep(1000 * id * 2);
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("TaskWithResult.call方法执行，id=" + id);
        return "TaskWithResult.call返回值：" + id;
    }
}

class CallableDemo {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for (int i = 0; i < 10; i++)
            results.add(exec.submit(new TaskWithResult(i)));
        for (Future<String> fs : results)
            try {
                // fs.get()处于当前线程，但不会马上执行，若相应线程没有返回结果，则对应的get()会等待。例如：线程5的call没有返回结果，则results.get(5)的get()不会执行，但是只要“线程非数字5”的call返回结果，则results.get(非数字5)的get()就会执行
                System.out.println(fs.get());
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
    }
}


class FibonacciSum
        implements Generator<Integer>, Callable<Integer> {
    private final int n;
    private int count;

    public FibonacciSum(int n) {
        this.n = n;
    }

    public Integer next() {
        return fib(count++);
    }

    private int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }

    public Integer call() {
        int sum = 0;
        for (int i = 0; i < n; i++)
            sum += next();
        return sum;
    }
}

class E05_FibonacciSum {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<Integer>> results =
                new ArrayList<Future<Integer>>();
        for (int i = 1; i <= 5; i++)
            results.add(exec.submit(new FibonacciSum(i)));
        Thread.yield();
        exec.shutdown();
        for (Future<Integer> fi : results)
            try {
                System.out.println(fi.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}


// P660
class SleepingTask2 implements Runnable {
    private static Random rnd = new Random();
    private final int sleep_time = rnd.nextInt(10) + 1;

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(sleep_time);
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e);
        }
        System.out.println(sleep_time);
    }
}

class E06_SleepingTask2 {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < Integer.parseInt("10"); i++)
            exec.execute(new SleepingTask2());
        Thread.yield();
        exec.shutdown();
    }
}


//P662
class SimpleDaemons implements Runnable {
    private int i;

    public static void main(String[] args) throws Exception {
        //for(int i = 0; i < 10; i++) {
        Thread daemon = new Thread(new SimpleDaemons());
        daemon.setDaemon(true); // 设置为守护进程，当main进程结束，守护进程结束。若不设置为守护进程，则此进程一直执行，一直打印
        daemon.start();
        //}
        print("All daemons started");
        TimeUnit.MILLISECONDS.sleep(10000);
    }

    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                print(Thread.currentThread() + "    " + i++ + "    " + this);
            }
        } catch (InterruptedException e) {
            print("sleep() interrupted");
        }
    }
}


//P664
class Daemon implements Runnable {
    private Thread[] t = new Thread[10];

    public void run() {
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new DaemonSpawn());
            t[i].start();
            printnb("DaemonSpawn " + i + " started, ");
        }
        for (int i = 0; i < t.length; i++)
            printnb("t[" + i + "].isDaemon() = " +
                    t[i].isDaemon() + ", ");
        while (true)
            Thread.yield();
    }
}

class DaemonSpawn implements Runnable {
    public void run() {
        while (true)
            Thread.yield();
    }
}

class Daemons {
    public static void main(String[] args) throws Exception {
        Thread d = new Thread(new Daemon());
        d.setDaemon(true);
        d.start();
        printnb("d.isDaemon() = " + d.isDaemon() + ", ");
        // Allow the daemon threads to
        // finish their startup processes:
        TimeUnit.SECONDS.sleep(10000);
    }
}


// P669
class FibonacciSum2 {
    private static ExecutorService exec;

    private static int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }

    public static Future<Integer> runTask(final int n) {
        assert exec != null;
        return exec.submit(new Callable<Integer>() {
            public Integer call() {
                int sum = 0;
                for (int i = 0; i < n; i++)
                    try {
                        TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        sum += fib(i);
                    }

                return sum;
            }
        });
    }

    public static synchronized void init() {
        if (exec == null)
            exec = Executors.newCachedThreadPool();
    }

    public static synchronized void shutdown() {
        if (exec != null)
            exec.shutdown();
        exec = null;
    }
}

class E10_FibonacciSum2 {
    public static void main(String[] args) {
        ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();
        FibonacciSum2.init();
        for (int i = 1; i <= 100; i++)
            results.add(FibonacciSum2.runTask(i));
        Thread.yield();
        FibonacciSum2.shutdown();
        for (Future<Integer> fi : results)
            try {
                System.out.println(fi.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}


//P670
class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            print(getName() + " 中断 " +
                    "isInterrupted(): " + isInterrupted());
        }
        print(" sleeper thread running***************" + getName());
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            print("Interrupted");
        }
        print(" Joiner thread running***************" + getName());
    }
}

class Joining {
    public static void main(String[] args) {
        Sleeper
                sleepy = new Sleeper("Sleepy", 1500),
                grumpy = new Sleeper("Grumpy", 1500);
        Joiner
                dopey = new Joiner("Dopey", sleepy),
                doc = new Joiner("Doc", grumpy);
        grumpy.interrupt();
    }
}


class Demo implements Runnable {
    String a;

    public Demo(String a) {
        this.a = a;
    }

    public void run() {
        for (int x = 0; x < 70; x++) {
            System.out.println(Thread.currentThread().toString() + "....." + x + "....." + a);
            Thread.yield();
        }
    }
}


class JoinDemo {
    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Demo("a"));
        Thread t2 = new Thread(new Demo("b"));
        t1.start();

        t1.join();  //t1线程执行完后，main面线程才执行，之后t2线程才执行。

        t2.start();

        System.out.println("over");
    }
}




// P675 - 676信息
abstract class IntGenerator {
    private volatile boolean canceled = false;
    public abstract int next();
    // Allow this to be canceled:
    public void cancel() { canceled = true; }
    public boolean isCanceled() { return canceled; }
}
class EvenChecker implements Runnable {
    private IntGenerator generator;
    private final int id;
    public EvenChecker(IntGenerator g, int ident) {
        generator = g;
        id = ident;
    }
    public void run() {
        while(!generator.isCanceled()) {  //线程中的generator是公共资源，generator.next()操作同一个变量
            int val = generator.next();
            if(val % 2 != 0) {
                System.out.println(val + " not even!");
                generator.cancel(); // Cancels all EvenCheckers
            }
        }
    }
    // Test any type of IntGenerator:
    public static void test(IntGenerator gp, int count) {
        System.out.println("Press Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < count; i++)
            exec.execute(new EvenChecker(gp, i));
        exec.shutdown();
    }
    // Default value for count:
    public static void test(IntGenerator gp) {
        test(gp, 10);
    }
}
class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;
    //public synchronized int next() {  //死锁
    //    ++currentEvenValue;
    //    Thread.yield();
    //    ++currentEvenValue;
    //    return currentEvenValue;
    //}
    public int next() {
        ++currentEvenValue; // Danger point here!
        ++currentEvenValue;
        return currentEvenValue;
    }
    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator()); //new EvenGenerator()是公共资源，next()方法操作同一个变量
    }
}










class E14_ManyTimers {
    public static void main(String[] args) throws Exception {

        int numOfTimers = Integer.parseInt("10");
        for(int i = 0; i < numOfTimers; i++) {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    System.out.println(System.currentTimeMillis());
                }
            }, numOfTimers - i);
        }
// Wait for timers to expire
        TimeUnit.MILLISECONDS.sleep(2 * numOfTimers);
        System.exit(0);
    }
}













// P688
class Pair { // Not thread-safe
    private int x, y;
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Pair() { this(0, 0); }
    public int getX() { return x; }
    public int getY() { return y; }
    public void incrementX() { x++; }
    public void incrementY() { y++; }
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
    public class PairValuesNotEqualException
            extends RuntimeException {
        public PairValuesNotEqualException() {
            super("Pair values not equal: " + Pair.this);
        }
    }
    // Arbitrary invariant -- both variables must be equal:
    public void checkState() {
        if(x != y)
            throw new PairValuesNotEqualException();
    }
}

// Protect a Pair inside a thread-safe class:
abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage =
            Collections.synchronizedList(new ArrayList<Pair>());
    public synchronized Pair getPair() {
        // Make a copy to keep the original safe:
        return new Pair(p.getX(), p.getY());
    }
    // Assume this is a time consuming operation
    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch(InterruptedException ignore) {}
    }
    public abstract void increment();
}

// Synchronize the entire method:
class PairManager1 extends PairManager {
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

// Use a critical section:
class PairManager2 extends PairManager {
    public void increment() {
        Pair temp;
        synchronized(this) {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}

class PairManipulator implements Runnable {
    private PairManager pm;
    public PairManipulator(PairManager pm) {
        this.pm = pm;
    }
    public void run() {
        while(true)
            pm.increment();
    }
    public String toString() {
        return "Pair: " + pm.getPair() +
                " checkCounter = " + pm.checkCounter.get();
    }
}

class PairChecker implements Runnable {
    private PairManager pm;
    public PairChecker(PairManager pm) {
        this.pm = pm;
    }
    public void run() {
        while(true) {
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}

class CriticalSection {
    // Test the two different approaches:
    static void
    testApproaches(PairManager pman1, PairManager pman2) {
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManipulator
                pm1 = new PairManipulator(pman1),
                pm2 = new PairManipulator(pman2);
        PairChecker
                pcheck1 = new PairChecker(pman1),
                pcheck2 = new PairChecker(pman2);
        exec.execute(pm1);
        exec.execute(pm2);
        exec.execute(pcheck1);
        exec.execute(pcheck2);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch(InterruptedException e) {
            System.out.println("Sleep interrupted");
        }
        System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
        System.exit(0);
    }
    public static void main(String[] args) {
        PairManager
                pman1 = new PairManager1(),
                pman2 = new PairManager2();
        testApproaches(pman1, pman2);
    }
}












// P690
class SingleSynch {
    public synchronized void f() {
        for(int i = 0; i < 5; i++) {
            print("f()");
            Thread.yield();
        }
    }
    public synchronized void g() {
        for(int i = 0; i < 5; i++) {
            print("g()");
            Thread.yield();
        }
    }
    public synchronized void h() {
        for(int i = 0; i < 5; i++) {
            print("h()");
            Thread.yield();
        }
    }
}
class TripleSynch {
    private Object syncObjectG = new Object();
    private Object syncObjectH = new Object();
    public synchronized void f() {    //this、syncObjectG、syncObjectH互不影响，一个线程执行public synchronized void f()的时候，其他线程可以执行public void g() 、 public void h()
        for(int i = 0; i < 500; i++) {
            print("f()");
            Thread.yield();
        }
    }
    public void g() {
        synchronized(syncObjectG) {
            for(int i = 0; i < 500; i++) {
                print("g()");
                Thread.yield();
            }
        }
    }
    public void h() {
        synchronized(syncObjectH) {
            for(int i = 0; i < 500; i++) {
                print("h()");
                Thread.yield();
            }
        }
    }
}
class E15_SyncObject {
    public static void main(String[] args) throws Exception {
        final SingleSynch singleSync = new SingleSynch();
        final TripleSynch tripleSync = new TripleSynch();
        print("Test SingleSynch...");
        Thread t1 = new Thread() {
            public void run() {
                singleSync.f();
            }
        };
        t1.start();
        Thread t2 = new Thread() {
            public void run() {
                singleSync.g();
            }
        };
        t2.start();
        singleSync.h();
        t1.join(); // Wait for t1 to finish its work
        t2.join(); // Wait for t2 to finish its work
        print("Test TripleSynch...");
        new Thread() {
            public void run() {
                tripleSync.f();
            }
        }.start();
        new Thread() {
            public void run() {
                tripleSync.g();
            }
        }.start();
        tripleSync.h();
    }
}















// P692
class Count {
    private int count = 0;
    private Random rand = new Random(47);
    // Remove the synchronized keyword to see counting fail:
    public  int increment() {
        int temp = count;
        if(rand.nextBoolean()) // Yield half the time
            Thread.yield();
        return (count = ++temp);
    }
    public synchronized int value() { return count; }
}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<Entrance> entrances =
            new ArrayList<Entrance>();
    private int number = 0;
    // Doesn't need synchronization to read:
    private final int id;
    private static volatile boolean canceled = false;
    // Atomic operation on a volatile field:
    public static void cancel() { canceled = true; }
    public Entrance(int id) {
        this.id = id;
        // Keep this task in a list. Also prevents
        // garbage collection of dead tasks:
        entrances.add(this);
    }
    public void run() {
        while(!canceled) {
            //synchronized(this) {
                ++number;
            //}
            print(this + " Entrance的Count的count = " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(10000);
            } catch(InterruptedException e) {
                print("sleep interrupted");
            }
        }
        print("Stopping " + this);
    }
    public  int getValue() { return number; }
    public String toString() {
        return "Entrance id = " + id + " ；Entrance number = " + getValue();
    }
    public static int getTotalCount() {
        return count.value();
    }
    public static int sumEntrances() {
        int sum = 0;
        for(Entrance entrance : entrances)
            sum += entrance.getValue();
        return sum;
    }
}

class OrnamentalGarden {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new Entrance(i));
        // Run for a while, then stop and collect the data:
        TimeUnit.SECONDS.sleep(300);
        Entrance.cancel();
        exec.shutdown();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            print("Some tasks were not terminated!");
        print("Total: " + Entrance.getTotalCount());
        print("Sum of Entrances: " + Entrance.sumEntrances());
    }
}












class Count1 {
    private int count = 0;
    private Random rand = new Random(47);
    // Remove the synchronized keyword to see counting fail:
    public synchronized int increment() {
        int temp = count;
        if(rand.nextBoolean()) // Yield half the time
            Thread.yield();
        return (count = ++temp);
    }
    public synchronized int value() { return count; }
}
class Sensor implements Runnable {
    private static Random rand = new Random(47);
    private static Count1 count = new Count1();
    private static List<Sensor> sensors =
            new ArrayList<Sensor>();
    private int number;
    private final int id;
    private static volatile boolean canceled = false;
    public static void cancel() { canceled = true; }
    public Sensor(int id) {
        this.id = id;
        sensors.add(this);
    }
    public void run() {
        while(!canceled) {
// Simulate a random occurence of an ionizing event
            if(rand.nextInt(5) == 0) {
                synchronized(this) { ++number; }
                count.increment();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(InterruptedException e) {
                print("sleep interrupted");
            }
        }
    }
    public synchronized int getValue() { return number; }
    public String toString() {
        return "Sensor " + id + ": " + getValue();
    }
    public static int getTotalCount() {
        return count.value();
    }
    public static int sumSensors() {
        int sum = 0;
        for(Sensor sensor : sensors)
            sum += sensor.getValue();
        return sum;
    }
}
class E17_RadiationCounter {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new Sensor(i));
        TimeUnit.SECONDS.sleep(3);
        Sensor.cancel();
        exec.shutdown();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            print("Some tasks were not terminated!");
        print("Total: " + Sensor.getTotalCount());
        print("Sum of Sensors: " + Sensor.sumSensors());
    }
}











// P696
class SleepBlocked implements Runnable {
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch(InterruptedException e) {
            print("SleepBlocked.run()中断异常");
        }
        print("SleepBlocked.run()退出");
    }
}

class IOBlocked implements Runnable {
    private InputStream in;
    public IOBlocked(InputStream is) { in = is; }
    public void run() {
        try {
            print("IOBlocked.run()的read()");
            in.read();
        } catch(IOException e) {
            if(Thread.currentThread().isInterrupted()) {
                print("IOBlocked.run() I/O异常");
            } else {
                throw new RuntimeException(e);
            }
        }
        print("IOBlocked.run()退出");
    }
}

class SynchronizedBlocked implements Runnable {
    public synchronized void f() {
        while(true) // Never releases lock
            Thread.yield();
    }
    public SynchronizedBlocked() {
        new Thread() {
            public void run() {
                f(); // Lock acquired by this thread
            }
        }.start();
    }
    public void run() {
        print("SynchronizedBlocked.run()运行，调用f()");
        f();
        print("SynchronizedBlocked.run()退出");
    }
}

class Interrupting {
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static void test(Runnable r) throws InterruptedException{
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        print("准备中断 " + r.getClass().getSimpleName());
        f.cancel(true); // Interrupts if running
        print("中断 " + r.getClass().getSimpleName());
    }
    public static void main(String[] args) throws Exception {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        print("退出系统");
        System.exit(0); // ... since last 2 interrupts failed
    }
}







// P699
class NonTask {
    static void longMethod() throws InterruptedException {
        TimeUnit.SECONDS.sleep(60); // Waits one minute
    }
}
class Task implements Runnable {
    public void run() {
        try {
            NonTask.longMethod();
        } catch (InterruptedException ie) {
            System.out.println(ie.toString());
        } finally {}
    }
}
class E18_Interruption {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Task());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        t.interrupt();
    }
}







// P699
class Entrance2 implements Runnable {
    private static Count count = new Count();
    private static List<Entrance2> entrances = new ArrayList<Entrance2>();
    private int number;
    private final int id;
    public Entrance2(int id) {
        this.id = id;
        entrances.add(this);
    }
    public void run() {
        for(;;) {
            synchronized(this) { ++number; }
            print(this + " Total: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(InterruptedException e) {
                print("Stopping " + this);
                return;
            }
        }
    }
    public synchronized int getValue() { return number; }
    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }
    public static int getTotalCount() {
        return count.value();
    }
    public static int sumEntrances() {
        int sum = 0;
        for(Entrance2 entrance : entrances)
            sum += entrance.getValue();
        return sum;
    }
}
class E19_OrnamentalGarden2 {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new Entrance2(i));
        TimeUnit.SECONDS.sleep(3);
        exec.shutdownNow();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            print("Some tasks were not terminated!");
        print("Total: " + Entrance2.getTotalCount());
        print("Sum of Entrances: " + Entrance2.sumEntrances());
    }
}













class LiftOff2 implements Runnable {
    protected int countDown = 5000;
    private static int taskCount;
    private final int id = taskCount++;
    public LiftOff2() {}
    public LiftOff2(int countDown) {
        this.countDown = countDown;
    }
    public String status() {
        return "#" + id + "(" +
                (countDown > 0 ? countDown : "Liftoff!") + "), ";
    }
    public void run() {
        while(countDown-- > 0) {
            if(Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted: #" + id);
                return;
            }
            System.out.print(status());
            Thread.yield();
        }
    }
}
class E20_InterruptCachedThreadPool {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new LiftOff2());
        Thread.yield();
        //exec.shutdownNow();
    }
}







//P700 https://www.zhihu.com/question/36771163
class BlockedMutex {
    private Lock lock = new ReentrantLock();
    public BlockedMutex() {
        //创建对象的时候就获取锁，并且不释放锁。
        lock.lock();
    }

    public void f() {
        try {
            // 调用后一直阻塞到获得锁，但是接受中断信号。由于构造函数一直没有释放锁，所以线程一直处于阻塞状态。
            lock.lockInterruptibly();
            print("lock acquired in f()");
        } catch(InterruptedException e) {
            print("BlockedMutex.f()发生中断，并且能获取锁");
        }
    }
}

class Blocked2 implements Runnable {
    BlockedMutex blocked = new BlockedMutex();
    public void run() {
        print("BlockedMutex.f()开始执行");
        blocked.f();
        print("Broken out of blocked call");
    }
}

class Interrupting2 {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked2());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("线程t，发出中断");
        t.interrupt();
    }
}











// P701
class Blocked21 implements Runnable {
    public void run() {
        print("Blocked21.run()开始执行");
    }
}

class Interrupting21 {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked21());
        t.start();
        TimeUnit.SECONDS.sleep(10);
        print("主线程");
    }
}








//P701
class NeedsCleanup {
    private final int id;
    public NeedsCleanup(int ident) {
        id = ident;
        print("NeedsCleanup对象创建： " + id);
    }
    public void cleanup() {
        print("NeedsCleanup对象清除： " + id);
    }
}

class Blocked3 implements Runnable {
    private volatile double d = 0.0;
    public void run() {
        try {
            while(!Thread.interrupted()) {
                // point1
                NeedsCleanup n1 = new NeedsCleanup(1);
                // Start try-finally immediately after definition
                // of n1, to guarantee proper cleanup of n1:
                try {
                    print("线程睡眠1s");
                    TimeUnit.SECONDS.sleep(1);
                    // point2
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    // Guarantee proper cleanup of n2:
                    try {
                        print("开始计算");
                        // 一个耗时的非阻塞操作，非阻塞状态不能被中断。
                        for(int i = 1; i < 2500000; i++){
                            print(i);
                            d = d + (Math.PI + Math.E) / d;
                        }
                        print("计算完成");
                    } finally {
                        //main设置sleep时间超过1000，线程处于运行状态，无法被中断，发生异常，则在此finally中清除n2
                        n2.cleanup();
                    }
                } finally {
                    //main设置sleep时间为1000，线程处于阻塞状态，此时中断线程，发生异常，并且finally中的这句代码一定会执行，一定可以清除n1
                    n1.cleanup();
                }
            }
            print("退出while循环");
        } catch(InterruptedException e) {
            print("中断异常");
        }
    }
}

class InterruptingIdiom {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        t.interrupt();
    }
}










//P704
class Car {
    private boolean waxOn = false;
    //打蜡
    public synchronized void waxed() {
        waxOn = true; // Ready to buff
        notifyAll();
    }
    //抛光
    public synchronized void buffed() {
        waxOn = false; // Ready for another coat of wax
        notifyAll();
    }
    //等待打蜡
    public synchronized void waitForWaxing()
            throws InterruptedException {
        while(waxOn == false)
            wait();
    }
    //等待抛光
    public synchronized void waitForBuffing()
            throws InterruptedException {
        while(waxOn == true)
            wait();
    }
}

class WaxOn implements Runnable {
    private Car car;
    public WaxOn(Car c) { car = c; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                printnb("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch(InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax On task");
    }
}

class WaxOff implements Runnable {
    private Car car;
    public WaxOff(Car c) { car = c; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                car.waitForWaxing();
                printnb("Wax Off! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch(InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax Off task");
    }
}

class WaxOMatic {
    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5); // Run for a while...
        exec.shutdownNow(); // Interrupt all tasks
    }
}









//P706
class E22_BusyWait {
    private static volatile boolean flag;
    private static int spins;
    public static void main(String[] args) throws Exception {
        Runnable r1 = new Runnable() {
            public void run() {
                for(;;) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10000);
                    } catch (InterruptedException e) { return; }
                    flag = true;
                }
            }
        };
        Runnable r2 = new Runnable() {
            public void run() {
                for(;;) {
// The busy-wait loop
                    while(!flag && !Thread.currentThread().isInterrupted())
                        spins++;
                    System.out.println("Spun " + spins + " times");
                    spins = 0;
                    flag = false;
                    if(Thread.interrupted())
                        return;
                }
            }
        };
    }
}


class E22_WaitNotify {
    public static void main(String[] args) throws Exception {
        final Runnable r1 = new Runnable() {
            public void run() {
                for(;;) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10000);
                        synchronized(this) { notify(); }
                    } catch(InterruptedException e) { return; }
                }
            }
        };
        Runnable r2 = new Runnable() {
            public void run() {
                for(;;) {
                    try {
                        synchronized(r1) { r1.wait(); }
                    } catch(InterruptedException e) { return; }
                    System.out.print("Cycled ");
                }
            }
        };
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(r1);
        exec.execute(r2);
        TimeUnit.SECONDS.sleep(1);
        exec.shutdownNow();
    }
}











// P709
class Blocker {
    synchronized void waitingCall() {
        try {
            while(!Thread.interrupted()) {
                wait();
                System.out.print(Thread.currentThread() + " ");
            }
        } catch(InterruptedException e) {
            // OK to exit this way
        }
    }
    synchronized void prod() { notify(); }
    synchronized void prodAll() { notifyAll(); }
}

class Task1 implements Runnable {
    static Blocker blocker = new Blocker();
    public void run() { blocker.waitingCall(); }
}

class Task2 implements Runnable {
    // notify()、notifyAll()只能唤醒等待特定锁的任务。blocker.prodAll()中使用了notifyAll()，锁为Task2中new出来的blocker。Task1中blocker.prod()、blocker.prodAll()锁都是Task1中blocker，不可能唤醒Task2.blocker.waitingCall()中的wait()。
    static Blocker blocker = new Blocker();
    public void run() { blocker.waitingCall(); }
}

class NotifyVsNotifyAll {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new Task1());
        exec.execute(new Task2());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean prod = true;
            public void run() {
                if(prod) {
                    System.out.print("\nnotify() ");
                    Task1.blocker.prod();
                    prod = false;
                } else {
                    System.out.print("\nnotifyAll() ");
                    Task1.blocker.prodAll();
                    prod = true;
                }
            }
        }, 400, 400); // Run every .4 second
        TimeUnit.SECONDS.sleep(5); // Run for a while...
        timer.cancel();
        System.out.println("\nTimer canceled");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print("Task2.blocker.prodAll() ");
        Task2.blocker.prodAll();
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("\nShutting down");
        exec.shutdownNow(); // Interrupt all tasks
    }
}












//P710
class Meal {
    private final int orderNum;
    public Meal(int orderNum) { this.orderNum = orderNum; }
    public String toString() { return "Meal " + orderNum; }
}

class WaitPerson implements Runnable {
    private Restaurant restaurant;
    public WaitPerson(Restaurant r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal == null)
                        wait(); // ... for the chef to produce a meal
                }
                print("Waitperson got " + restaurant.meal);
                synchronized(restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notifyAll(); // Ready for another
                }
            }
        } catch(InterruptedException e) {
            print("WaitPerson interrupted");
        }
    }
}

class Chef implements Runnable {
    private Restaurant restaurant;
    private int count = 0;
    public Chef(Restaurant r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal != null)
                        wait(); // ... for the meal to be taken
                }
                if(++count == 10) {
                    print("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                printnb("Order up! ");
                synchronized(restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            print("Chef interrupted");
        }
    }
}

class Restaurant {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    public Restaurant() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }
    public static void main(String[] args) {
        new Restaurant();
    }
}












//P711
class FlowQueue<T> {
    private Queue<T> queue = new LinkedList<T>();
    private int maxSize;
    public FlowQueue(int maxSize) { this.maxSize = maxSize; }
    public synchronized void put(T v) throws InterruptedException {
        while(queue.size() >= maxSize)
            wait();
        queue.offer(v);
        maxSize++;
        notifyAll();
    }
    public synchronized T get() throws InterruptedException {
        while(queue.isEmpty())
            wait();
        T returnVal = queue.poll();
        maxSize--;
        notifyAll();
        return returnVal;
    }
}
class Item {
    private static int counter;
    private int id = counter++;
    public String toString() { return "Item " + id; }
}
// Produces Item objects
class Producer implements Runnable {
    private int delay;
    private FlowQueue<Item> output;
    public Producer(FlowQueue<Item> output, int sleepTime) {
        this.output = output;
        delay = sleepTime;
    }
    public void run() {
        for(;;) {
            try {
                output.put(new Item());
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch(InterruptedException e) { return; }
        }
    }
}
// Consumes any object
class Consumer implements Runnable {
    private int delay;
    private FlowQueue<?> input;
    public Consumer(FlowQueue<?> input, int sleepTime) {
        this.input = input;
        delay = sleepTime;
    }
    public void run() {
        for(;;) {
            try {
                System.out.println(input.get());
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch(InterruptedException e) { return; }
        }
    }
}
class E24_ProducerConsumer {
    public static void main(String[] args) throws Exception {
        int producerSleep = Integer.parseInt("10");
        int consumerSleep = Integer.parseInt("11");
        FlowQueue<Item> fq = new FlowQueue<Item>(100);
        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.execute(new Producer(fq, producerSleep));
        exec.execute(new Consumer(fq, consumerSleep));
        TimeUnit.SECONDS.sleep(2);
        exec.shutdownNow();
    }
}













//P712
class Car2 {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean waxOn = false;
    public void waxed() {
        lock.lock();
        try {
            waxOn = true; // Ready to buff
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void buffed() {
        lock.lock();
        try {
            waxOn = false; // Ready for another coat of wax
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void waitForWaxing() throws InterruptedException {
        lock.lock();
        try {
            while(waxOn == false)
                condition.await();
        } finally {
            lock.unlock();
        }
    }
    public void waitForBuffing() throws InterruptedException{
        lock.lock();
        try {
            while(waxOn == true)
                condition.await();
        } finally {
            lock.unlock();
        }
    }
}

class WaxOn2 implements Runnable {
    private Car2 car;
    public WaxOn2(Car2 c) { car = c; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                printnb("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch(InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax On task");
    }
}

class WaxOff2 implements Runnable {
    private Car2 car;
    public WaxOff2(Car2 c) { car = c; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                car.waitForWaxing();
                printnb("Wax Off! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch(InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax Off task");
    }
}

class WaxOMatic2 {
    public static void main(String[] args) throws Exception {
        Car2 car = new Car2();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff2(car));
        exec.execute(new WaxOn2(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}












//P713
class Meal2 {
    private final int orderNum;
    public Meal2(int orderNum) { this.orderNum = orderNum; }
    public String toString() { return "Meal2 " + orderNum; }
}
class WaitPerson2 implements Runnable {
    private Restaurant2 restaurant;
    public WaitPerson2(Restaurant2 r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal == null)
                        wait(); // ... for the chef to produce a meal
                }
                print("Waitperson2 got " + restaurant.meal);
                synchronized(restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notifyAll(); // Ready for another
                }
            }
        } catch(InterruptedException e) {
            print("WaitPerson interrupted");
        }
    }
}
class Chef2 implements Runnable {
    private Restaurant2 restaurant;
    private int count = 0;
    public Chef2(Restaurant2 r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal != null)
                        wait(); // ... for the meal to be taken
                }
                if(++count == 10) {
                    print("Out of food, closing");
                    restaurant.exec.shutdownNow();
                    return;
                }
                printnb("Order up! ");
                synchronized(restaurant.waitPerson) {
                    restaurant.meal = new Meal2(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            print("Chef interrupted");
        }
    }
}
class Restaurant2 {
    Meal2 meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson2 waitPerson = new WaitPerson2(this);
    Chef2 chef = new Chef2(this);
    public Restaurant2() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }
    public static void main(String[] args) {
        new Restaurant2();
    }
}
class E25_Restaurant {
    public static void main(String[] args) {
        new Restaurant2();
    }
}













//P714
class LiftOffRunner implements Runnable {
    private BlockingQueue<LiftOff> rockets;
    public LiftOffRunner(BlockingQueue<LiftOff> queue) {
        rockets = queue;
    }
    public void add(LiftOff lo) {
        try {
            rockets.put(lo);
        } catch(InterruptedException e) {
            print("Interrupted during put()");
        }
    }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run(); // Use this thread
            }
        } catch(InterruptedException e) {
            print("Waking from take()");
        }
        print("Exiting LiftOffRunner");
    }
}

class TestBlockingQueues {
    static void getkey() {
        try {
            // Compensate for Windows/Linux difference in the
            // length of the result produced by the Enter key:
            new BufferedReader( new InputStreamReader(System.in) ).readLine();
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void getkey(String message) {
        print(message);
        getkey();
    }
    static void test(String msg, BlockingQueue<LiftOff> queue) {
        print(msg);
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
        for(int i = 0; i < 5; i++)
            runner.add(new LiftOff(5));
        getkey("Press 'Enter' (" + msg + ")");
        t.interrupt();
        print("Finished " + msg + " test");
    }
    public static void main(String[] args) {
        test("LinkedBlockingQueue", // Unlimited size
                new LinkedBlockingQueue<LiftOff>());
        test("ArrayBlockingQueue", // Fixed size
                new ArrayBlockingQueue<LiftOff>(3));
        test("SynchronousQueue", // Size of 1
                new SynchronousQueue<LiftOff>());
    }
}





//P715
class LiftOffRunner1 implements Runnable {
    private BlockingQueue<LiftOff> rockets;
    public LiftOffRunner1(BlockingQueue<LiftOff> queue) {
        rockets = queue;
    }
    public void add(LiftOff lo) throws InterruptedException {
        rockets.put(lo);
    }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run(); // Use this thread
            }
        } catch(InterruptedException e) {
            print("Waking from take()");
        }
        print("Exiting LiftOffRunner1");
    }
}
class LiftOffProducer implements Runnable {
    private LiftOffRunner1 runner;
    public LiftOffProducer(LiftOffRunner1 runner) {
        this.runner = runner;
    }
    public void run() {
        try {
            for(int i = 0; i < 5; i++)
                runner.add(new LiftOff(5));
        } catch(InterruptedException e) {
            print("Waking from put()");
        }
        print("Exiting LiftOffProducer");
    }
}
class E28_TestBlockingQueues2 {
    private static void getkey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void getkey(String message) {
        print(message);
        getkey();
    }
    private static void
    test(String msg, BlockingQueue<LiftOff> queue) {
        ExecutorService exec = Executors.newFixedThreadPool(2);
        print(msg);
        LiftOffRunner1 runner = new LiftOffRunner1(queue);
        LiftOffProducer producer = new LiftOffProducer(runner);
        exec.execute(runner);
        exec.execute(producer);
        getkey("Press 'ENTER' (" + msg + ")");
        exec.shutdownNow();
        print("Finished " + msg + " test");
    }
    public static void main(String[] args) {
        test("LinkedBlockingQueue", // Unlimited size
                new LinkedBlockingQueue<LiftOff>());
        test("ArrayBlockingQueue", // Fixed size
                new ArrayBlockingQueue<LiftOff>(3));
        test("SynchronousQueue", // Size of 1
                new SynchronousQueue<LiftOff>());
    }
}











// P723
class TaskPortion implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;
    TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            doWork();
            latch.countDown();
        } catch(InterruptedException ex) {
            // Acceptable way to exit
        }
    }
    public void doWork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
        print(this + "completed");
    }
    public String toString() {
        return String.format("%1$-3d ", id);
    }
}

// Waits on the CountDownLatch:
class WaitingTask implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch latch;
    WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            latch.await();
            print("Latch barrier passed for " + this);
        } catch(InterruptedException ex) {
            print(this + " interrupted");
        }
    }
    public String toString() {
        return String.format("WaitingTask %1$-3d ", id);
    }
}

class CountDownLatchDemo {
    static final int SIZE = 100;
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        // All must share a single CountDownLatch object:
        CountDownLatch latch = new CountDownLatch(SIZE);
        for(int i = 0; i < 10; i++) {
            //创建10个等待任务，WaitingTask.run()方法中调用了latch.await()，会阻塞。必须等待latch.countDown()执行SIZE（100）次（即TaskPortion.run()执行一百次），这样new CountDownLatch(SIZE)持有的计数器变成0，WaitingTask.run()方法将可以继续运行。
            exec.execute(new WaitingTask(latch));
        }

        for(int i = 0; i < SIZE; i++) {
            exec.execute(new TaskPortion(latch));
        }
        print("Launched all tasks");
        exec.shutdown(); // Quit when all tasks complete
    }
}









//http://www.importnew.com/21889.html

/**
 * public CountDownLatch(int count) {  };  //参数count为计数值
 * public void await() throws InterruptedException { };
 * //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
 public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };
 //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
 public void countDown() { };  //将count值减1
 */
class Test {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(){
            public void run() {
                try {
                    System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        new Thread(){
            public void run() {
                try {
                    System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}









// P724
class Entrance3 implements Runnable {
    private final CountDownLatch latch;
    private static Count count = new Count();
    private static List<Entrance3> entrances = new ArrayList<Entrance3>();
    private int number;
    private final int id;
    private static volatile boolean canceled;
    public static void cancel() { canceled = true; }
    public Entrance3(CountDownLatch ltc, int id) {
        latch = ltc;
        this.id = id;
        entrances.add(this);
    }
    public void run() {
        while(!canceled) {
            synchronized(this) { ++number; }
            print(this + " Total: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(InterruptedException e) {
                print("sleep interrupted");
            }
        }
        latch.countDown();
        print("Stopping " + this);
    }
    public synchronized int getValue() { return number; }
    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }
    public static int getTotalCount() {
        return count.value();
    }
    public static int sumEntrances() {
        int sum = 0;
        for(Entrance3 entrance : entrances)
            sum += entrance.getValue();
        return sum;
    }
}
class E32_OrnamentalGarden3 {
    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new Entrance3(latch, i));
        TimeUnit.SECONDS.sleep(3);
        Entrance3.cancel();
        exec.shutdown();
        latch.await(); // Wait for results
        print("Total: " + Entrance3.getTotalCount());
        print("Sum of Entrances: " + Entrance3.sumEntrances());
    }
}












class Test11 {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }
    static class Writer extends Thread{
        static int num = 0;
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"线程，"+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println(Thread.currentThread().getName()+"线程，"+"写入数据完毕，等待其他线程写入完毕");
                num = Integer.parseInt(StringUtils.substring(Thread.currentThread().getName(), -1));

                //只有线程0、1、2执行await，但是线程3没有等待，线程3会继续往下执行。线程0、1、2一直卡住
                if (num == 0 || num == 1 || num == 2){
                    cyclicBarrier.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"线程，"+"继续处理其他任务...");
        }
    }
}

class Test12 {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N,new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程"+Thread.currentThread().getName());
            }
        });

        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}




class Te334 {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);

        for(int i=0;i<N;i++) {
            if(i<N-1)
                new Writer(barrier).start();
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Writer(barrier).start();
            }
        }
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                try {
                    cyclicBarrier.await(2000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }
}




class Tessst {
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CyclicBarrier重用");
        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");

                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }
}



// P724
class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;
    public Horse(CyclicBarrier b) { barrier = b; }
    public synchronized int getStrides() { return strides; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    strides += rand.nextInt(3); // Produces 0, 1 or 2
                }
                barrier.await();
            }
        } catch(InterruptedException e) {
            // A legitimate way to exit
        } catch(BrokenBarrierException e) {
            // This one we want to know about
            throw new RuntimeException(e);
        }
    }
    public String toString() { return "Horse " + id + " "; }
    public String tracks() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < getStrides(); i++)
            s.append("*");
        s.append(id);
        return s.toString();
    }
}

class HorseRace {
    static final int FINISH_LINE = 75;
    private List<Horse> horses = new ArrayList<Horse>();
    private ExecutorService exec =
            Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    public HorseRace(int nHorses, final int pause) {
        barrier = new CyclicBarrier(nHorses, new Runnable() {
            public void run() {
                StringBuilder s = new StringBuilder();
                for(int i = 0; i < FINISH_LINE; i++)
                    s.append("="); // The fence on the racetrack
                print(s);
                for(Horse horse : horses)
                    print(horse.tracks());
                for(Horse horse : horses)
                    if(horse.getStrides() >= FINISH_LINE) {
                        print(horse + "won!");
                        exec.shutdownNow();
                        return;
                    }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch(InterruptedException e) {
                    print("barrier-action sleep interrupted");
                }
            }
        });
        for(int i = 0; i < nHorses; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }
    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 200;
        if(args.length > 0) { // Optional argument
            int n = new Integer(args[0]);
            nHorses = n > 0 ? n : nHorses;
        }
        if(args.length > 1) { // Optional argument
            int p = new Integer(args[1]);
            pause = p > -1 ? p : pause;
        }
        new HorseRace(nHorses, pause);
    }
}



