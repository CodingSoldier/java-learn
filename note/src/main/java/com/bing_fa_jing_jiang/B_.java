package com.bing_fa_jing_jiang;

import org.junit.Test;

public class B_ {

    @Test
    public void threadRunnable(){

        /**
         * 因为原始的Thread类中，run方法会调用传入的Runnable中的run
         * 而我们创建的匿名类覆盖了run方法，覆盖方法中并没有调用Runnable的run
         */
        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("runnable中的run()");
            }
        }){
            @Override
            public void run() {
                System.out.println("匿名类Thread中的run()");
            }
        }.start();
    }
}
