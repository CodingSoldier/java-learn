package com.example.reactor0.a03error;

import com.example.reactor0.a01example.Sir;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author chenpq05
 * @since 2022/7/7 14:40
 */
@Component
public class Handler {

    private Mono<String> sayHelloFallback() {
        return Mono.just("hello, Stranger");
    }

    private Mono<String> sayHello(ServerRequest request) {
        try {
            return Mono.just("Hello，"+request.queryParam("name").get());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<ServerResponse> handleWithErrorReturn(ServerRequest request) {
        return sayHello(request)
                .onErrorReturn("Hello, Stranger")
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(s));
    }

    public Mono<ServerResponse> handleWithErrorResumeAndDynamicFallback(ServerRequest request) {
        return sayHello(request)
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(s))
                .onErrorResume(e -> (Mono.just("Hi, I looked around for your name but found: "+ e.getMessage()))
                        .flatMap(s -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON).
                                bodyValue(s)));
    }

    public Mono<ServerResponse> handleWithResumeAndFallbackMethod(ServerRequest request) {
        return sayHello(request)
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(s))
                .onErrorResume(e -> sayHelloFallback()
                        .flatMap(s -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(s)));
    }

    public Mono<ServerResponse> handleWithErrorResumeAndCustomException(ServerRequest request) {
        return ServerResponse.ok()
                .body(sayHello(request)
                        .onErrorResume(e -> Mono.error(new RuntimeException("自定义异常"))), String.class);

    }

    public Mono<ServerResponse> handleWithGloabalError(ServerRequest request) {
        return ServerResponse.ok().body(sayHello(request), String.class);
    }

}
