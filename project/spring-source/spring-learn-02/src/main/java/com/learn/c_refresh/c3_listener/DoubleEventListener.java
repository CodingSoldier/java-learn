package com.learn.c_refresh.c3_listener;

/**
 * @author chenpiqian
 * @date: 2020-07-23
 */
public class DoubleEventListener implements EventListener{

	@Override
	public void processEvent(Event event) {
		if ("double".equals(event.getType())){
			System.out.println("@@@@double事件");
		}
	}

}
