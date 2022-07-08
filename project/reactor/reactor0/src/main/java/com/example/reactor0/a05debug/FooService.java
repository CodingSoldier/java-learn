package com.example.reactor0.a05debug;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

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
                .doOnError(error -> log.error("The following error happened on processFoo method!", error))
                .subscribe();
    }

    public static void processFooInAnotherScenario(Flux<Foo> flux) {
        flux.map(FooNameHelper::substringFooName)
                .map(FooQuantityHelper::divideFooQuantity)
                .subscribe();
    }

    public static void main(String[] args) throws Exception {
        Foo foo = new Foo();
        foo.setId(1);
        foo.setQuantity(90);
        foo.setFormattedName("FormattedName");
        Flux<Foo> flux = Flux.just(foo);
        processFoo(flux);
        TimeUnit.SECONDS.sleep(100L);
    }

}
