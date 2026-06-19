package com.example.iot.exception;

/**
 * 调用请求等待时间超过配置值时抛出的异常。
 */
public class InvokeTimeoutException extends RuntimeException {

    /**
     * 创建调用超时异常。
     *
     * @param msgId 超时的消息 ID
     */
    public InvokeTimeoutException(long msgId) {
        super("调用请求等待超时，msgId=" + msgId);
    }
}
