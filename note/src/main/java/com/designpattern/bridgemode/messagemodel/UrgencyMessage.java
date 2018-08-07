package com.designpattern.bridgemode.messagemodel;

public class UrgencyMessage extends AbstractMessage {
    public UrgencyMessage(MessageImplementor impl){
        super(impl);
    }

    @Override
    public void sendMessage(String message, String toUser) {
        message = "加急："+message;
        super.sendMessage(message, toUser);
    }

    public Object watch(String messageId){
        return null;
    }
}
