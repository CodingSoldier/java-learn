package com.zs.web.reactive.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @auther: madison
 * @date: 2018/10/22 20:33
 * @description:
 */

@SpringBootApplication
@RestController
public class WebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class);
    }

    @GetMapping("mvc")
    public String mvc() {
        println("MVC");
        return "MVC";
    }

    @GetMapping("mono")
    public Mono<String> mono() {
        println("mono");
        return Mono.just("Mono");
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
//        return RouterFunctions.route(request -> {
//                    //判断请求是否匹配
//                    URI uri = request.uri();
//                    return "/hello-world".equals(uri.getPath());
//                },
//                //绑定实现
//                request -> {
//                    Mono<ServerResponse> mono = ServerResponse.status(HttpStatus.OK)
//                            .body(Mono.just("Hello,World"), String.class);
//                    return mono;
//                });
        return route(GET("/hello-world"), this::hellWorld);
    }

    public Mono<ServerResponse> hellWorld(ServerRequest serverRequest) {
        println("hellWorld");
        return ServerResponse.status(HttpStatus.OK)
                .body(Mono.just("Hello,World"), String.class);
    }

    private static void println(String mseeage) {
        System.out.println("[" + Thread.currentThread().getName() + "]: " + mseeage);
    }
}
