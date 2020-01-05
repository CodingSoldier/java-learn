package com.mmall.concurrency;

import com.mmall.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@NotThreadSafe
public class CountExample {

    public static void main(String[] args) throws Exception {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        //
        //exec.scheduleAtFixedRate(new Runnable() {
        //    @Override
        //    public void run() {
        //        System.out.println("schedule run");
        //    }
        //}, 1, 3, TimeUnit.SECONDS);
        //
        exec.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("11111111");
            }
        }, 2, 4, TimeUnit.SECONDS);
    }

}
