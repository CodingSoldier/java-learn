package com.example.dflux;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootTest
class Flux01 {

/**
 Flux代表一个包含0~N个元素的响应式序列
 Mono代表一个包含0/1个元素的结果
 */

    @Test
    void contextLoads() {

        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6);
        //没有输出
        //flux.subscribe();

        //依次输出123456
        Integer[] integers = {1, 2, 3, 4, 5, 6};
        Flux<Integer> arrayFlux = Flux.fromArray(integers);
        //arrayFlux.subscribe(System.out::println);

        //依次输出123456
        Flux<Integer> listFlux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6));
        //listFlux.subscribe(System.out::println, System.err::println);

        //依次输出123456 完成
        Flux<Integer> streamFlux = Flux.fromStream(Stream.of(1, 2, 3, 4, 5, 6));
        //streamFlux.subscribe(System.out::println, System.err::println,
        //        () -> System.out.println("完成"));

        //背压，消费端只要3个元素，输出123
        //streamFlux.subscribe(System.out::println, System.err::println,
        //        () -> System.out.println("结束"),
        //        subscription -> subscription.request(3));

        //streamFlux.subscribe(new BeiYaSubscriber());

        // map操作符遍历flux序列并转换
        //flux.map(i -> i*2).subscribe(System.out::println);

        // 将每个元素生成flux序列再输出
        //arrayFlux.flatMap(i -> flux).subscribe(System.out::println);

        //过滤出符合条件的flux元素
        //listFlux.filter(i -> i>3).subscribe(System.out::println);

        // 合并操作
        //Flux<Integer> fluxFlux = Flux.from(flux);
        //Flux.zip(fluxFlux, streamFlux).subscribe(zip -> System.out.println(zip.getT1() + zip.getT2()));

        /**
         Schedulers.immediate()  当前线程
         Schedulers.single()     可重复的单线程
         Schedulers.elastic()    弹性线程池
         Schedulers.parallel()   固定大小线程池
         Schedulers.fromExecutorService()  自定义线程池

         publishOn将上游信号传给下游，同时改变后续操作符的执行线程，直到下一个publishOn出现在这个链上
         subscribeOn作用于向上的订阅链，无论出于操作链的什么位置，他都会影响到源头的线程执行环境，但不会影响到后续的publishOn
         */
        flux.map(i -> {
            System.out.println(Thread.currentThread().getName() + "-map1");
            return i * 3;
        }).publishOn(Schedulers.elastic()).map(i -> {
            System.out.println(Thread.currentThread().getName() + "-map2");
            return i/3;
        }).subscribe(it -> System.out.println(Thread.currentThread().getName() + "-" + it));

/**
reactor和java8 stream的区别：
    reactor使用push模式，服务端推送数据给客户端
    stream使用pull模式，客服端主动向服务端请求数据
 */
    }

    // 背压class
    class BeiYaSubscriber extends BaseSubscriber<Integer>{
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            System.out.println("订阅时触发");
            subscription.request(1);
        }

        @Override
        protected void hookOnNext(Integer value) {
            //消费4个数据，就取消消费
            if (value == 4){
                cancel();
            }
            System.out.println(value);
            request(1);
        }
    }

}
