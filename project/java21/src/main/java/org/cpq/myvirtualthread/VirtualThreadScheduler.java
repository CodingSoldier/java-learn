package org.cpq.myvirtualthread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadScheduler {

    public static ScopedValue<MyVirtualThread> MY_VIRTUAL_THREAD = ScopedValue.newInstance();

    private Queue<MyVirtualThread> queue = new ConcurrentLinkedQueue<>();
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public void start() {
        while (true) {
            if (!queue.isEmpty()) {
                MyVirtualThread virtualThread = queue.remove();
                executor.submit(() -> ScopedValue
                        .where(MY_VIRTUAL_THREAD, virtualThread)
                        .run(virtualThread::run)
                );

            }
        }
    }

    public void schedule(MyVirtualThread virtualThread) {
        queue.add(virtualThread);
    }
}
