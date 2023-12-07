package com.cpq.jpa01;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *@ClassName: ThreadPoolConfig
 * @author v-xuk19
 *
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        //ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        MdcThreadPoolTaskExecutor executor = new MdcThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(8);
        // 设置最大线程数
        executor.setMaxPoolSize(100);
        // 设置队列容量
        executor.setQueueCapacity(30);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(300);
        // 设置默认线程名称
        executor.setThreadNamePrefix("thread-obs");
        // 设置拒绝策略rejection-policy：当pool已经达到max size的时候，如何处理新任务 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     *  异步线程池submit时，传递父线程的trackId
     *
     */
    @Slf4j
    public static class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor{

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            String trackIdKey = "mdc_key";
            Map<String, String> context = MDC.getCopyOfContextMap();
            // 将RequestAttributes对象设置为子线程共享
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            RequestContextHolder.setRequestAttributes(sra, true);
            return super.submit(() -> {
                // 将父线程的MDC内容传给子线程
                T result = null;
                if (context != null && !StringUtils.isEmpty(context.get(trackIdKey))) {
                    MDC.setContextMap(context);
                } else {
                    MDC.put(trackIdKey, UUID.randomUUID().toString().replace("-", ""));
                }
                try {
                    result = task.call();
                } finally {
                    try {
                        MDC.clear();
                    } catch (Exception e2) {
                        log.warn("mdc clear exception.", e2);
                    }
                }
                return result;
            });
        }

    }


}
