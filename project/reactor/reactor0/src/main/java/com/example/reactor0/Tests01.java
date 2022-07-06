package com.example.reactor0;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
class Tests01 {

    ArrayList<Integer> elems = new ArrayList<>();

    @Test
    void contextLoads() {
        Flux<Integer> just = Flux.just(1, 2);

        Optional<Object> empty = Optional.empty();
        System.out.println(empty);
    }

    @Test
    void fn1() {
        Publisher<Integer> just = Mono.just(1);
        // just.subscribe();
    }

    @Test
    void fn2() {
        ArrayList<Integer> list = new ArrayList<>();
        Flux.just(1, 2, 4, 4)
                .log()
                .subscribe(e -> list.add(e));
        System.out.println(list);
    }

    @Test
    void fn3() {
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {

                    // 订阅事件
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        log.info("#######{}", subscription);
                    }

                    // 数据到达事件
                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("@@@@@@@{}" + integer);
                    }

                    // 订阅异常
                    @Override
                    public void onError(Throwable throwable) {
                        log.info("!!!!!!{}", throwable);
                    }

                    // 订阅完成
                    @Override
                    public void onComplete() {
                        log.info("ccccccccc");
                    }
                });
    }

    @Test
    public void fn4(){
        ArrayList<Integer> list = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
            .log()
            .subscribe(new Subscriber<Integer>() {
                private Subscription s;
                int onNextAmount;

                @Override
                public void onSubscribe(Subscription subscription) {
                    this.s = subscription;
                    s.request(2);
                }

                @Override
                public void onNext(Integer integer) {
                    list.add(integer);
                    onNextAmount++;
                    if (onNextAmount % 2 == 0) {
                        s.request(2);
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    @Test
    public void fn5() {
        Flux.just(1, 2, 3, 5)
                .log()
                .map(i -> i * 2)
                .subscribe(e -> elems.add(e));
        System.out.println(elems);
    }

    @Test
    public void fn6() {
        ArrayList<String> l = new ArrayList<>();
        Flux.just(1, 2, 3, 4, 5)
                .log()
                .map(i -> i*2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First Flux : %d, Second Flux: %d", one, two))
                .subscribe(e -> l.add(e));
        System.out.println(l);
    }

    @Test
    public void fn7() {
        ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        }).publish();
        publish.subscribe(e -> System.out.println("#######"+e));
        publish.subscribe(e -> System.out.println("@@@@@@@"+e));

        publish.connect();
    }

    @Test
    public void fn8() {
        ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        }).publish();
        publish.sample(Duration.ofSeconds(2)).subscribe(e -> System.out.println("#######"+e));
        publish.sample(Duration.ofSeconds(10)).subscribe(e -> System.out.println("@@@@@@@"+e));
        publish.connect();
    }


    @Test
    public void fn9(){
        Flux.just(1, 2, 3, 4)
        .log()
        .map(i -> i*2)
        .subscribeOn(Schedulers.parallel())
        .subscribe(elems::add);
    }

}
