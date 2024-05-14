// package com.cpq.mybatispulslearn.config;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import java.time.LocalDateTime;
//
// @Slf4j
// @Component
// public class TestTask {
//
//     @Autowired
//     private TaskConfig taskConfig;
//
//     @Scheduled(cron = "#{@taskConfig.getCron() ?: '0 0/12 * * * ?'}")
//     public void aaa() {
//         LocalDateTime now = LocalDateTime.now();
//         log.info("{}################", now);
//     }
//
//
//
// }
