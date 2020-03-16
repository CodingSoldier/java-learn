package com.example.ee4springservlet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * 异步IO
 */
@RestController
public class HelloWorldAsyncController {

    /**
     * DeferredResult异步结果对象
     */
    @GetMapping("/hello-world")
    public DeferredResult<String> helloWorld() {
        DeferredResult<String> result = new DeferredResult<>();
        result.setResult("hello, world");

        /**
         * 非阻塞回调
         * controller方法线程 与 onCompletion回调线程 都是同一个线程
         */
        result.onCompletion(() -> {
            String str = "返回结果给客户端后，本代码才执行。当前线程："+Thread.currentThread().getName();
            System.out.println(str);
        });

        System.out.println("方法执行完毕。当前线程："+Thread.currentThread().getName());
        return result;
    }

    /**
     * DeferredResult不设置data就返回，方法执行就当做超时处理
     */
    @GetMapping("/timeout")
    public DeferredResult<String> timeout() {
        DeferredResult<String> result = new DeferredResult<>();
        System.out.println("开始执行");
        System.out.println("controller当前线程："+Thread.currentThread().getName());

        /**
         * 发生超时，controller方法中的线程和回调函数中的线程不一样了
         * 在timeout的时候会发生线程切换
         * 并且，onTimeout先执行，onCompletion后执行
         * onTimeout类似于try-catch
         * onCompletion类似于finally
         *
         */
        result.onCompletion(() -> {
            System.out.println("返回结果给客户端后，本代码才执行。当前线程："+Thread.currentThread().getName());
        });
        result.onTimeout(()->{
            System.out.println("超时。当前线程："+Thread.currentThread().getName());
        });

        System.out.println("执行结束");
        return result;
    }

    /**
     * 使用callable实现异步回调
     */
    @GetMapping("/callable")
    public Callable<String> callable() {
        System.out.println("controller方法线程 "+Thread.currentThread().getName());

        return () -> {
            /**
             * callable线程和controller方法线程不是同一个线程
             * 但是如果本线程阻塞，controller方法也会阻塞，callable还没return返回值
             */
            System.out.println("callable线程 "+Thread.currentThread().getName());
            return "Hello, world";
        };
    }

    /**
     * 使用CompletionStage实现异步操作
     */
    @GetMapping("/completion/stage")
    public CompletionStage<String> completion() {
        System.out.println("controller方法线程 "+Thread.currentThread().getName());

        return CompletableFuture.supplyAsync(() -> {
            // 回调方法是新线程执行
            System.out.println("CompletableFuture.supplyAsync方法线程 "+Thread.currentThread().getName());
            return "Hello world";
        });
    }


}
