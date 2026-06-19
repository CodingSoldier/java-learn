package com.example.iot.exception;

/**
 * 待处理请求数量达到上限时抛出的异常。
 */
public class PendingRequestLimitExceededException extends RuntimeException {

    /**
     * 创建待处理请求数量超限异常。
     *
     * @param maxPending 配置的最大待处理请求数
     */
    public PendingRequestLimitExceededException(int maxPending) {
        super("待处理请求数量达到上限，maxPending=" + maxPending);
    }
}
