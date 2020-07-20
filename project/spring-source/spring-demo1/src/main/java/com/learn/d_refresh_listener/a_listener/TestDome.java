package com.learn.d_refresh_listener.a_listener;

/**
 * @author chenpiqian
 * @date: 2020-07-17
 */
public class TestDome {

    public static void main(String[] args) {

        SingleClickEventListener singleClickEventListener = new SingleClickEventListener();
        DoubleClickEventListener doubleClickEventListener = new DoubleClickEventListener();

        EventSource eventSource = new EventSource();
        eventSource.register(singleClickEventListener);
        eventSource.register(doubleClickEventListener);

        Event event = new Event();
        event.setType("doubleClick");
        event.setType("singleClick");

        eventSource.publishEvent(event);

    }
}
