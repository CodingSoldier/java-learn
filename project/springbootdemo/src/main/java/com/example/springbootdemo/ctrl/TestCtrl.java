package com.example.springbootdemo.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2020-01-04
 */
@RestController
@RequestMapping("/test")
public class TestCtrl {

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(
            3,
            5,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2));

    @GetMapping("/1")
    public Object test1(){
        for(int i=1; i<20; i++) {
            System.out.println("提交第" + i + "个任务!");
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(">>>task is running=====");
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return "success";
    }


    @GetMapping("/2")
    public Object test2(){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(">>>再次执行任务=====");
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return "success";
    }

}
