package com.learn.c_refresh.c3_listener;

/**
 * @author chenpiqian
 * @date: 2020-07-23
 */
public class SingleEventListener implements EventListener{

	@Override
	public void processEvent(Event event) {
		if ("single".equals(event.getType())){
			System.out.println("@@@@single事件");
		}
	}

}
