package org.cpq.virtualthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class VirtualThread {

    public static void main(String[] args) throws Exception {
        /**
         * 虚拟线程与平台线程执行速度比较
         */
        // List<Thread> threadList = IntStream.range(0, 10000).mapToObj(new IntFunction<Thread>() {
        //     @Override
        //     public Thread apply(int value) {
        //         // 创建虚拟线程
        //         return Thread.ofVirtual().unstarted(() -> {
        //             System.out.println(Thread.currentThread());
        //         });
        //         // // 创建物理线程
        //         // return Thread.ofPlatform().unstarted(() -> {
        //         //     System.out.println(Thread.currentThread());
        //         // });
        //     }
        // }).toList();
        // long begin = System.currentTimeMillis();
        // for (Thread thread : threadList) {
        //     thread.start();
        //     thread.join();
        // }
        // long end = System.currentTimeMillis();
        // System.out.println("耗时：" + (end - begin));

        // 创建虚拟线程的4种方法
        Thread.startVirtualThread(() -> System.out.println("创建虚拟线程1"));

        Thread.ofVirtual().name("虚拟线程2").start(() -> System.out.println("创建虚拟线程2"));
        Thread thread21 = Thread.ofVirtual().name("虚拟线程2-1").unstarted(() -> System.out.println("创建虚拟线程2-1"));
        thread21.start();

        ThreadFactory factory = Thread.ofVirtual().factory();
        factory.newThread(() -> System.out.println("通过ThreadFactory创建虚拟线程")).start();

        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            service.submit(() -> System.out.println("通过ExecutorService创建虚拟线程"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimeUnit.SECONDS.sleep(10);
    }

}
