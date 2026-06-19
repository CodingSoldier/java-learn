package com.example.iot.common;

import cn.hutool.core.lang.Snowflake;
import org.springframework.stereotype.Component;

/**
 * 基于雪花算法生成分布式唯一的消息 ID。
 */
@Component
public class MsgIdGenerator {

    private final Snowflake snowflake = new Snowflake();

    /**
     * 返回下一个消息 ID。
     *
     * @return 生成的消息 ID
     */
    public long nextId() {
        return snowflake.nextId();
    }
}
