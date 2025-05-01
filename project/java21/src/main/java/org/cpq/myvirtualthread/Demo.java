package org.cpq.myvirtualthread;

public class Demo {
    public static final VirtualThreadScheduler SCHEDULER = new VirtualThreadScheduler();

    /**
     * 模拟虚拟线程：
     *
     * 创建虚拟线程类 MyVirtualThread
     * 创建调度器 VirtualThreadScheduler
     *      使用queue保存虚拟线程
     *      使用平台线程运行虚拟线程
     *      使用ScopedValue替代线程的ThreadLocal
     * 创建WaitingOperation实现虚拟线程等待一段时间后继续运行
     *      Continuation.yield(MyVirtualThread.SCOPE); 实现虚拟线程等待
     *      Timer实现虚拟线程重新加入调度器VirtualThreadScheduler，继续运行Continuation.yield(MyVirtualThread.SCOPE)中断点后面的代码
     *
     * main方法会创建100个虚拟线程，每个虚拟线程运行到一半会调用WaitingOperation.perform()模拟IO等待，
     * 等待一段时间后继续运行，此时虚拟线程可能会在其他平台线程中运行
     */
    public static void main(String[] args) {
        new Thread(SCHEDULER::start).start();
        for (int i = 0; i < 100; i++) {
            MyVirtualThread vt1 = new MyVirtualThread(() -> {
                System.out.println("1.1");
                WaitingOperation.perform("发送HTTP请求，需要时间", 200);
                System.out.println("1.2");
            });
            MyVirtualThread vt2 = new MyVirtualThread(() -> {
                System.out.println("2.1");
                WaitingOperation.perform("查询数据库，需要时间", 300);
                System.out.println("2.2");
            });
            SCHEDULER.schedule(vt1);
            SCHEDULER.schedule(vt2);
        }
    }
}
