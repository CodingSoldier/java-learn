package com.example.cpq.fspringreactive;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class FluxDemo {

    public static void main(String[] args) {
        println("运行");

        //Flux.just("A", "B", "C")
        //        //.publishOn(Schedulers.elastic())
        //        .map(value -> "+"+value)
        //        .subscribe(
        //                FluxDemo::println,
        //                FluxDemo::println,
        //                () -> println("完成操作"),
        //                subscription -> {
        //                    subscription.request(Integer.MAX_VALUE);
        //                    subscription.cancel();
        //                }
        //        );

        Flux.just("A", "B", "C")
            .map(value -> "+"+value)
            .subscribe(new Subscriber<String>() {

                private Subscription subscription;
                private int count = 0;

                @Override
                public void onSubscribe(Subscription subscription) {
                    subscription.request(2);
                }

                @Override
                public void onNext(String s) {
                    if (count == 2){
                        throw new RuntimeException("故意抛异常");
                    }
                    println(s);
                    count++;
                    subscription.request(2);
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

    }

    private static void println(Object object) {
        String threadName = Thread.currentThread().getName();
        System.out.println("[线程: " + threadName + "]" + object);
    }

}
