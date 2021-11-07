package com.example.app3;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequestMapping("/test")
@RestController
@Slf4j
public class TestController {

    Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/get")
    public String get() throws Exception {
        Random random = new Random();
        int i = random.nextInt(500) + 100;
        TimeUnit.MILLISECONDS.sleep(i);
        // v1、v2版本的打印信息不同
        String r = "版本V1，睡眠时间---" + i;
        //String r = "版本V2222，睡眠时间---"+i;
        logger.info(r);
        return r;
    }

    @GetMapping("/01")
    public String get01() throws Exception {

        log.error("###########错误输出");
        RuntimeException e = new RuntimeException("exception");
        log.error("###########RuntimeException", e);
        log.info("###########info输出");

        return "r";
    }

}
