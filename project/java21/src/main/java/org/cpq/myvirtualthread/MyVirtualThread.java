package org.cpq.myvirtualthread;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.concurrent.atomic.AtomicInteger;

public class MyVirtualThread {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    public static final ContinuationScope SCOPE = new ContinuationScope("我的虚拟线程");

    private Continuation cont;
    private int id;

    public MyVirtualThread(Runnable runnable) {
        id = COUNTER.getAndIncrement();
        cont = new Continuation(SCOPE, runnable);
    }

    public void run() {
        System.out.println("MyVirtualThread " + id + " is running on" + Thread.currentThread());
        cont.run();
    }

}
