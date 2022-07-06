package com.example.reactor01._01notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * @author chenpq05
 * @since 2022/7/5 17:23
 */
@Controller
public class NotificationController {

    @Autowired
    private EventBus eventBus;

    @GetMapping("/startNotification/{param}")
    public String startNotification(@PathVariable Integer param) {
        for (int i = 0; i < param; i++) {
            NotificationData data = new NotificationData();
            data.setId(i);
            eventBus.notify("notificationConsumer", Event.wrap(data));
            System.out.println("Notification " + i + ": notification task submitted successfully");
        }
        return "done";
    }

}
