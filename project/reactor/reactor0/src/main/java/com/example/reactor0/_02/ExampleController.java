package com.example.reactor0._02;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Controller
public class ExampleController {

    @RequestMapping("hello/{who}")
    @ResponseBody
    public Mono<String> hello(@PathVariable String who) {
        return Mono.just(who)
                .map(e -> "hello " + who);
    }


    @RequestMapping("data/{who}")
    @ResponseBody
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

}
