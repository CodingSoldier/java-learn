package com.thinkinginjava.thinkinginjavablog;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class T1 {
}

class Task implements Runnable {
    private String name;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {

        }

        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "**" + this.name);
        }
        Thread.yield();
    }
}

//能够返回结果
class TaskCallable<T> implements Callable {
    private int id;

    public TaskCallable(int id) {
        this.id = id;
    }

    @Override
    public T call() {
        try {
            //延迟返回结果
            TimeUnit.SECONDS.sleep(id * 2);
        } catch (Exception e) {

        }
        return (T) ("id:" + id);
    }
}

