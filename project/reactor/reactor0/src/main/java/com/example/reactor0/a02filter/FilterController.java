package com.example.reactor0.a02filter;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FilterController {

    @RequestMapping("/players/{name}")
    public Mono<String> getName(@PathVariable String name) {
        return Mono.just(name)
                .map(e -> "hello " + name);
    }


}
