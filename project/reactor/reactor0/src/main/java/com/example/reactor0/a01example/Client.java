package com.example.reactor0.a01example;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpq05
 * @since 2022/7/7 9:41
 */
public class Client {
    WebClient client = WebClient.create("http://localhost:8080");

    @Test
    public void test1() throws Exception{
        Flux<Sir> employeeFlux = client.get()
                .uri("/sir/list")
                .retrieve()
                .bodyToFlux(Sir.class);

        employeeFlux.subscribe(e -> System.out.println("##########" + e));
        TimeUnit.SECONDS.sleep(5);
    }
}
