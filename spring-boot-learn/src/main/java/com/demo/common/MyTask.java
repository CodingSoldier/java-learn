package com.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyTask {

    @Scheduled(cron = "*/5 * * * * ?")
    public void execute() {
        log.info("thread id:{},FixedPrintTask execute times", Thread.currentThread().getId());
        throw new RuntimeException("异常");
    }
}
