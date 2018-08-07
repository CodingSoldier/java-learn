package com.designpattern.bridgemode.messagemodel;

public class MessageSMS implements MessageImplementor {
    @Override
    public void send(String message, String toUser) {
        System.out.println("使用系统内短信的方法，发送消息："+message+"给："+toUser);
    }
}
