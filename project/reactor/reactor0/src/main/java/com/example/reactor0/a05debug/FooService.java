package com.example.reactor0.a05debug;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpq05
 * @since 2022/7/8 11:43
 */
@Slf4j
public class FooService {

    public static void processFoo(Flux<Foo> flux) {
        flux.map(FooNameHelper::concatFooName)
                .map(FooNameHelper::substringFooName)
                .log()
                .map(FooReporter::reportResult)
                .doOnError(error -> log.error("异常方法", error))
                .subscribe(foo -> {
                    log.debug("完成processing，foo的id是{}", foo.getId());
                }, error -> {
                    log.error("subscribe异常发生 {}", error);
                });
    }

    public static void processFooThread(Flux<Foo> flux) {
        flux.log()
            .subscribeOn(Schedulers.newSingle("five-single-starter"))
            .map(FooNameHelper::concatAndSubstringFooName)
            .publishOn(Schedulers.newSingle("five-single-foo"))
            .map(FooNameHelper::concatAndSubstringFooName)
            .map(FooQuantityHelper::divideFooQuantity)
            .map(foo -> FooReporter.reportResult(foo, "FIVE-SINGLE"))
            .publishOn(Schedulers.newSingle("five-single-bar"))
            .map(FooNameHelper::concatAndSubstringFooName)
            .doOnError(error -> log.error("Approach 5-single failed!", error))
            .subscribe();
    }

    public static void processUsingApproachFourWithCheckpoint(Flux<Foo> flux) {
        log.info("starting approach four!");

        flux.map(FooNameHelper::concatAndSubstringFooName)
                .checkpoint("CHECKPOINT 1")
                .map(FooNameHelper::concatAndSubstringFooName)
                .map(FooQuantityHelper::divideFooQuantity)
                .checkpoint("CHECKPOINT 2", true)
                .map(foo -> FooReporter.reportResult(foo, "FOUR"))
                .map(FooNameHelper::concatAndSubstringFooName)
                .doOnError(error -> log.error("Approach 4 failed!", error))
                .subscribe();
    }

    public static void processFooList(Flux<Foo> flux) {
        flux.map(FooNameHelper::concatFooName)
                .map(FooNameHelper::substringFooName)
                .log()
                .map(FooReporter::reportResult)
                    .doOnError(error -> {
                        log.error("打印信息", error);
                    })
                    .subscribe();
    }

    public static void processFooInAnotherScenario(Flux<Foo> flux) {
        flux.map(FooNameHelper::substringFooName)
                .map(FooQuantityHelper::divideFooQuantity)
                .subscribe();
    }

    public static void main(String[] args) throws Exception {
        // Flux<Integer> reactiveStream = Flux.range(1, 5).log();
        // Flux<Integer> reactiveStream = Flux.range(1, 5).log().take(3);
        Flux<Integer> reactiveStream = Flux.range(1, 5).take(3).log();

        reactiveStream.subscribe();


    }

}
