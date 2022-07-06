package com.example.reactor01._01notification;

/**
 * @author chenpq05
 * @since 2022/7/5 17:16
 */
public interface NotificationService {

    void initiateNotification(NotificationData data) throws InterruptedException;

}
