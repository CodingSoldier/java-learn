package com.learn.c_refresh.c3_listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenpiqian
 * @date: 2020-07-23
 */
public class EventSource {
	private List<EventListener> eventListeners = new ArrayList<>();

	public void registry(EventListener eventListener){
		eventListeners.add(eventListener);
	}

	public void processEvent(Event event){
		for (EventListener eventListener : eventListeners) {
			eventListener.processEvent(event);
		}
	}

}
