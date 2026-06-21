package com.example.iot.common;

import cn.hutool.core.lang.Snowflake;
import org.springframework.stereotype.Component;

/**
 * 基于雪花算法生成分布式唯一的消息 ID。
 * <p>
 * 返回 Snowflake ID 的十进制字符串。
 */
@Component
public class MsgIdGenerator {

    private final Snowflake snowflake = new Snowflake();

    /**
     * 返回下一个消息 ID 的十进制字符串。
     *
     * @return Snowflake ID 的十进制字符串
     */
    public String nextId() {
        return Long.toString(snowflake.nextId());
    }
}
