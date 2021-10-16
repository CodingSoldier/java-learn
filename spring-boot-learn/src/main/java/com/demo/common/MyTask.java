package com.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyTask {

    @Value("${test.val}")
    private String tv;

    @Scheduled(cron = "*/5 * * * * ?")
    public void execute() {
        System.out.println(tv);

        log.info("thread id:{},FixedPrintTask execute times", Thread.currentThread().getId());
        throw new RuntimeException("异常");
    }
}
