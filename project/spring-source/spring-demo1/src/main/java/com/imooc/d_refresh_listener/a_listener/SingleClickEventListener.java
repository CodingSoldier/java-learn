package com.imooc.d_refresh_listener.a_listener;

/**
 * @author chenpiqian
 * @date: 2020-07-17
 */
public class SingleClickEventListener implements EventListener {

    @Override
    public void processEvent(Event event) {
        if ("singleClick".equals(event.getType())){
            System.out.println("####单击被触发了#####");
        }
    }

}
