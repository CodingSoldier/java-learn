package com.thinkinginjavablog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TTT {
}






// https://blog.csdn.net/u010606397/article/details/80466316
//线程池
class ThreadPool {
    public static void main(String[] args) {

        ////newCachedThreadPool每次execute会创建一个线程。书中建议首选这种，但一些文章不建议中这种。
        //ExecutorService es = Executors.newCachedThreadPool();
        //for (int i = 0; i < 3; i++) {
        //    es.execute(new Task("newCachedThreadPool"));
        //}
        //es.shutdown();  //必须执行shutdown()，确保线程运行完后退出
        ////执行器实例shutdown()之后无法再运行新的任务，再次execute()会抛异常
        ////es.execute(new Task("本任务不会运行"));


        ////newFixedThreadPool通过参数指定线程池中的线程数量
        //ExecutorService es = Executors.newFixedThreadPool(2);
        //for (int i = 0; i < 3; i++) {
        //    es.execute(new Task("newFixedThreadPool"));
        //}
        //es.shutdown();


        ////就像是线程数为1的newFixedThreadPool
        //ExecutorService es = Executors.newSingleThreadExecutor();
        //for (int i = 0; i < 3; i++) {
        //    es.execute(new Task("newSingleThreadExecutor"));
        //}
        //es.shutdown();


        //Callable可以返回结果，Runnable不能返回结果
        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<String>> results = new ArrayList<>();
        for (int i=0; i<3; i++){
            results.add(es.submit(new TaskCallable<String>(i)));
        }
        String str = "";
        for (Future<String> fs:results){
            try {
                //TaskCallable任务中设置了延迟，执行get的时候还没有结果返回。这种情况下get()将会阻塞，直至结果赶回完毕。
                str = fs.get();
                System.out.println(str);
            }catch (Exception e){

            }finally {
                es.shutdown();
            }
            System.out.println("get()阻塞，本输出延迟执行");
        }
        System.out.println("for循环外最后执行");

    }
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
        }catch (Exception e){

        }

        for (int i=0; i<3; i++){
            System.out.println(Thread.currentThread().getName() + "**" + this.name);
        }
        Thread.yield();
    }
}

//能够返回结果
class TaskCallable<T> implements Callable{
    private int id;

    public TaskCallable(int id) {
        this.id = id;
    }

    @Override
    public T call(){
        try {
            //延迟返回结果
            TimeUnit.SECONDS.sleep(id*2);
        }catch (Exception e){

        }
        return (T)("id:"+id);
    }
}

