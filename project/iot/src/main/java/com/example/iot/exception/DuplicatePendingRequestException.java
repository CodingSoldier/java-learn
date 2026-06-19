package com.example.iot.exception;

/**
 * 相同消息 ID 已经处于等待状态时抛出的异常。
 */
public class DuplicatePendingRequestException extends RuntimeException {

    /**
     * 创建消息 ID 重复异常。
     *
     * @param msgId 重复的消息 ID
     */
    public DuplicatePendingRequestException(long msgId) {
        super("待处理请求重复，msgId=" + msgId);
    }
}
