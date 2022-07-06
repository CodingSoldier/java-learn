package com.example.reactor01._01notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.fn.Consumer;


/**
 * @author chenpq05
 * @since 2022/7/5 17:19
 */
@Component
public class NotificationConsumer implements Consumer<Event<NotificationData>> {

    @Autowired
    private NotificationService notificationService;

    @Override
    public void accept(Event<NotificationData> event) {
        NotificationData data = event.getData();
        try {
            notificationService.initiateNotification(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
