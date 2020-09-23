package com.cpq.bingfa.threadpool;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class D_Run {

    @Test
    public void submitTest() throws InterruptedException, ExecutionException {
        Integer num;

        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future<Integer> future = threadPool.submit(() -> {
            TimeUnit.SECONDS.sleep(3);
            return 1;
        });

        //// 阻塞线程，直到返回结果
        //num = future.get();
        //System.out.println(num);

        /**
         * 主线程睡眠10秒，future早已返回结果，后面可以通过 future.get() 获取结果
         */
        TimeUnit.SECONDS.sleep(5);

        num = future.get();
        System.out.println(num);

    }

    public static void main(String[] args) throws Exception{
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = sdf.parse( "2020-09-21 14:05:28" );
        System.out.println(date);
        System.out.println(date.getTime());

        Date date1 = sdf.parse( "2020-09-21 14:12:28 " );
        System.out.println(date1);
        System.out.println(date1.getTime());
    }

}
