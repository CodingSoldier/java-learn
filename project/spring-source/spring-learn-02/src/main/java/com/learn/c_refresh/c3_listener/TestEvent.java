package com.learn.c_refresh.c3_listener;

/**
 * 监听器模式
 */
public class TestEvent {

	public static void main(String[] args) {
		DoubleEventListener doubleEventListener = new DoubleEventListener();
		SingleEventListener singleEventListener = new SingleEventListener();

		EventSource eventSource = new EventSource();
		eventSource.registry(doubleEventListener);
		eventSource.registry(singleEventListener);

		Event single = new Event("single");
		Event aDouble = new Event("double");

		eventSource.processEvent(single);
		eventSource.processEvent(aDouble);
	}

}
