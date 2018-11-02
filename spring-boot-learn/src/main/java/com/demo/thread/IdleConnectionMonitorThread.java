package com.demo.thread;



// 用于监控空闲的连接池连接
public final class IdleConnectionMonitorThread extends Thread {
    private volatile boolean shutdown;

    private static final long MONITOR_INTERVAL_MS = 20000000000000l;
    private static final int IDLE_ALIVE_MS = 5000;

    //public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
    //    super();
    //    this.connMgr = connMgr;
    //    this.shutdown = false;
    //}

    public IdleConnectionMonitorThread() {
        super();
        //this.connMgr = null;
        this.shutdown = false;
        System.out.println("构造函数");
    }

    @Override
    public void run() {
        try {
            System.out.println("run-----"+Thread.currentThread().getName());
            while (!shutdown) {
                synchronized (this) {
                    wait(MONITOR_INTERVAL_MS);
                    // 关闭无效的连�?
                    //connMgr.closeExpiredConnections();
                    // 关闭空闲时间超过IDLE_ALIVE_MS的连�?
                    //connMgr.closeIdleConnections(IDLE_ALIVE_MS, TimeUnit.MILLISECONDS);
                    //System.out.println("run");
                    //System.out.println("run-----"+Thread.currentThread().getName());
                    System.out.println("while-----"+Thread.currentThread().getName());
                }
            }
        } catch (InterruptedException e) {
            System.out.println("中断*****************************************");
            e.printStackTrace();
            //Thread.currentThread().interrupt();
        }
    }

    // 关闭后台连接
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            //System.out.println("shutdown-----"+Thread.currentThread().getName());
            //Thread.currentThread().interrupt();
            notifyAll();
        }
    }

}
