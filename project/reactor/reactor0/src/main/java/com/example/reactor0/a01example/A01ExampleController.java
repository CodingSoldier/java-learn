package com.example.reactor0.a01example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@RestController
public class A01ExampleController {

    @RequestMapping("hello/{who}")
    public Mono<String> hello(@PathVariable String who) {
        return Mono.just(who)
                .map(e -> "hello " + who);
    }


    @RequestMapping("data/{who}")
    public Mono<ResponseEntity<Sir>> hello11(@PathVariable String who) {
        Sir sir = new Sir();
        sir.setFirstName(who);
        return Mono.just(sir)
                .filter(e -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return true;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity
                        .status(404).body(null));
    }

    @GetMapping("sir/list")
    public Flux<Sir> getAllEmployees() {
        Sir sir = new Sir();
        sir.setFirstName("11111");
        Sir sir2 = new Sir();
        sir2.setFirstName("2222");
        return Flux.just(sir, sir2);
    }

}
