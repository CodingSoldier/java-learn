package com.designpattern.bridgemode.message;

public class CommonMessageSMS implements Message {
    @Override
    public void send(String message, String toUser) {
        System.out.println("使用系统内短消息的方式，发送消息"+message+"给"+toUser);
    }
}
