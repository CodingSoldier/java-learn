package com.example.reactor0.a02filter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpq05
 * @since 2022/7/7 9:41
 */
@Slf4j
public class Client02 {
    WebClient client = WebClient.create("http://localhost:8080");

    @Test
    public void users01() throws Exception {
        client.get()
                .uri("/players/baeldung")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(e -> log.info("!!!!!!!!!!{}", e));

        TimeUnit.SECONDS.sleep(2L);
    }
}
