package com.example.dflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DemoController {

    /**
     * webflux注解写法
     */
    @GetMapping("demo")
    public Mono<String> demo(){
        return Mono.just("demo");
    }

}
