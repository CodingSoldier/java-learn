package com.example.iot.mqtt;

/**
 * MQTT 主题常量。
 */
public final class MqttTopics {

    /**
     * 服务调用请求主题。
     */
    public static final String INVOKE_TOPIC = "/sys/servie/invoke";

    /**
     * 服务调用回复主题。
     */
    public static final String INVOKE_REPLY_TOPIC = "/sys/servie/invoke_reply";

    private MqttTopics() {
    }
}
