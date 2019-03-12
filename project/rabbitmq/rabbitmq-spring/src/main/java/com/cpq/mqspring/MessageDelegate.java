package com.cpq.mqspring;

public class MessageDelegate {

	public void handleMessage(byte[] messageBody) {
		System.err.println("默认方法, 消息内容:" + new String(messageBody));
	}

	public void consumeMessage(byte[] messageBody) {
		System.err.println("consumeMessage, 消息内容:" + new String(messageBody));
	}

	public void consumeMessage(String message) {
		System.err.println("consumeMessage(String message), 消息内容:" + message);
	}


}
