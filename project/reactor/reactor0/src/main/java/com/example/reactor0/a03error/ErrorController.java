package com.example.reactor0.a03error;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ErrorController {

    @RequestMapping("/hello")
    public Mono<String> getName(String name) {
        return Mono.just(name)
                .map(e -> "hello " + name);
    }

    @RequestMapping("/global")
    public Mono<String> global(String name) {
        if ("异常".equals(name)) {
            throw new RuntimeException("抛出异常");
        }
        return Mono.just(name)
                .map(e -> "hello " + name);
    }


}
