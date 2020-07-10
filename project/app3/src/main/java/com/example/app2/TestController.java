package com.example.app2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/get")
    public String get() throws Exception{
        Random random = new Random();
        int i = random.nextInt(10);
        TimeUnit.SECONDS.sleep(i);
        return "睡眠时间-2222 -"+i;
    }

}
