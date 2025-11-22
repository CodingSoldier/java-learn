package com.cpq.enums;

/**
 * @Description: 发送SSE的消息类型
 * @Author 风间影月
 */
public enum SSEMsgType {

    MESSAGE("message", "单词发送的普通类型消息"),
    ADD("add",  "消息追加，适用于流式stream推送"),
    FINISH("finish", "消息完成"),
    CUSTOM_EVENT("custom_event", "单词发送的普通类型消息"),
    DONE("done", "单词发送的普通类型消息");    // ChatGLM v4

    public final String type;
    public final String value;

    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }

}
