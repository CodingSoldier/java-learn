package org.cpq.myvirtualthread;

import jdk.internal.vm.Continuation;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingOperation {
   public static void perform(String name, int duration) {
       MyVirtualThread myVirtualThread = VirtualThreadScheduler.MY_VIRTUAL_THREAD.get();
       System.out.println("Waiting for "+name+" for "+duration+" milliseconds");
       Timer timer = new Timer();
       timer.schedule(new TimerTask() {
           @Override
           public void run() {
               // 等待duration毫秒后，将虚拟线程重新加入调度器队列，再次运行，
               // 再次运行的点是Continuation.yield之后的代码，即WaitingOperation.perform()之后的代码
               Demo.SCHEDULER.schedule(myVirtualThread);
               timer.cancel();
           }
       }, duration);

       // 虚拟线程进入等待
       Continuation.yield(MyVirtualThread.SCOPE);
   }
}
