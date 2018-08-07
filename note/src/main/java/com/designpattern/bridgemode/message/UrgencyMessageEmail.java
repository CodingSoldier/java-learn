package com.designpattern.bridgemode.message;

public class UrgencyMessageEmail implements UrgencyMessage {
    @Override
    public Object watch(String messageId) {
        return null;
    }

    @Override
    public void send(String message, String toUser) {
        message = "加急"+message;
        System.out.println("使用邮件短消息的方法，发送消息"+message+"给"+toUser);
    }
}
