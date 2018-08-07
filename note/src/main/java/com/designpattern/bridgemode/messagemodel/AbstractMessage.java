package com.designpattern.bridgemode.messagemodel;

public abstract class AbstractMessage {
    MessageImplementor impl;
    public AbstractMessage(MessageImplementor impl){
        this.impl = impl;
    }
    public void sendMessage(String message, String toUser){
        this.impl.send(message, toUser);
    }

}
