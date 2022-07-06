package com.example.reactor01;

import org.springframework.stereotype.Service;

/**
 * @author chenpq05
 * @since 2022/7/5 17:17
 */
@Service
public class NotificationServiceimpl implements NotificationService {
    @Override
    public void initiateNotification(NotificationData data) throws InterruptedException {
        System.out.println("Notification service started for "
                + "Notification ID: " + data.getId());

        Thread.sleep(5000);

        System.out.println("Notification service ended for "
                + "Notification ID: " + data.getId());
    }
}
