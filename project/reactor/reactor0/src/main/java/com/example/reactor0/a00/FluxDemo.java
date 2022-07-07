package com.example.reactor0.a00;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

/**
 * @author chenpq05
 * @since 2022/7/6 10:49
 */
public class FluxDemo {

    @Test
    public void test01() {
        Random random = new Random();
        Flux.just(1, 2, 3, 4,5,6,7)
                .map(i -> {
                    if (i == 3) {
                        throw new RuntimeException("i == 3");
                    }
                    return i;
                }).subscribe(
                        System.out::println,
                        System.out::println,
                        () -> System.out.println("complete"));
    }

    @Test
    public void test02() {
        Flux.generate(() -> 0, (value, sink) -> {
            if (value == 3) {
                sink.complete();
            } else {
                sink.next("value = " + value);
            }
            return value + 1;
        }).subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("complete"));
    }

    @Test
    public void test03() {
        Flux.generate(() -> 0, (value, sink) -> {
            if (value == 3) {
                sink.complete();
            } else {
                sink.next("value = " + value);
            }
            return value + 1;
        }).subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("complete"));
    }

    @Test
    public void test04() throws Exception {
        Flux.range(0, 10)
                .publishOn(Schedulers.immediate())
                .subscribe(System.out::println);

        Flux.range(0, 10)
                .publishOn(Schedulers.single())
                .subscribe(System.out::println);

        Flux.range(0, 10)
                .publishOn(Schedulers.elastic())
                .subscribe(System.out::println);

        Flux.range(0, 10)
                .publishOn(Schedulers.parallel())
                .subscribe(System.out::println);

        Thread.currentThread().join();

    }


}
















