package com.example.iot.common;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 生成单节点唯一的消息 ID。
 */
@Component
public class MsgIdGenerator {

    private final AtomicLong sequence = new AtomicLong(System.currentTimeMillis() * 1_000L);

    /**
     * 返回下一个消息 ID。
     *
     * @return 生成的消息 ID
     */
    public long nextId() {
        return sequence.incrementAndGet();
    }
}
