package com.learn.d_refresh_listener.a_listener;

/**
 * @author chenpiqian
 * @date: 2020-07-17
 */
public class DoubleClickEventListener implements EventListener {
    @Override
    public void processEvent(Event event) {
        if ("doubleClick".equals(event.getType())){
            System.out.println("#####双击被触发了####");
        }
    }
}
