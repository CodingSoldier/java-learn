package com.zs.spring.reactive.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @auther: madison
 * @date: 2018/10/21 11:30
 * @description:
 */
public class FluxDemo {

    public static void main(String[] args) throws Exception {
        println("运行...");
        // 发布 A->B->C`         3
        Flux.just("A", "B", "C")
//                .publishOn(Schedulers.elastic()) // 线程池切换
                .map(value -> "+" + value) // "A" -> "+"
//                .subscribe(
//                        FluxDemo::println, // 数据消费 = onNext(T)
//                        FluxDemo::println, // 异常处理 = onError(Throwable)
//                        () -> { // onComplete()
//                            println("完成操作! ");  //完成回调
//                        },
//                        subscription -> {//背压操作 = onSubscribe(subscription)
//                            subscription.request(Integer.MAX_VALUE); // 请求元素的数量
//                            subscription.cancel(); // 取消上游传输数据到下游
//                        }
//                )
                .subscribe(new Subscriber<>() {

                    private Subscription subscription;
                    private int count = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        if (count == 2) {
                            throw new RuntimeException("故意抛异常");
                        }
                        println(s);
                        count++;
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        println(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        println("完成操作");
                    }
                });
//        Thread.sleep(1000L);
//        println("Hello,World");
    }

    private static void println(Object object) {
        String threadName = Thread.currentThread().getName();
        System.out.println("[线程: " + threadName + "]" + object);
    }
}
