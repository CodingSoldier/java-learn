package com.example.bspringboot.i_exception_report;

public class ShutdownHookTest {

    public static void main(String[] args) {
        System.out.println("开始");
        Thread closeJVM = new Thread(() -> {
            //try {
            //    TimeUnit.SECONDS.sleep(2);
            //}catch (InterruptedException e){
            //    e.printStackTrace();
            //}
            int i = 0;
            while (i<Integer.MAX_VALUE){
                i++;

            }
            System.out.println("close jvm，在jvm销毁前执行一个线程");
        });
        // 在jvm销毁前执行一个线程
        Runtime.getRuntime().addShutdownHook(closeJVM);
        System.out.println("程序结束");

        // 移除钩子方法
        //Runtime.getRuntime().removeShutdownHook(closeJVM);
    }

}
