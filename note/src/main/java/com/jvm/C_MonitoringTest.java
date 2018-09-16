package com.jvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class C_MonitoringTest {

    static class OOMObject{
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException{
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num ; i++){
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    //线程死循环演示
    public static void createBusyThread(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true);
            }
        }, "testBusyThread");
        thread.start();
    }

    // 线程锁等待
    public static void createLockThread(final Object lock){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    try {
                        lock.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        },"testLockThread");
        thread.start();
    }

    // 死锁等待
    static class SynAddRunalbe implements Runnable{
        int a, b;

        public SynAddRunalbe(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)){
                synchronized (Integer.valueOf(b)){
                    System.out.println(a + b);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception{
        Thread.sleep(5*1000);

        // P117 内存管理演示
        // -Xms100m -Xmx100m -XX:+UseSerialGC
        //fillHeap(1000);
        //new BufferedReader(new InputStreamReader(System.in)).readLine();


        // P119 线程监控演示
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //br.readLine();
        //createBusyThread();
        //br.readLine();
        //
        //Object obj = new Object();
        //createLockThread(obj);


        // P121 死锁演示
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        for (int i=0; i<100; i++){
            new Thread(new SynAddRunalbe(1, 2)).start();
            new Thread(new SynAddRunalbe(2, 1)).start();
        }

    }


}

