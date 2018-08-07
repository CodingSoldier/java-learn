package com.designpattern.bridgemode.messagemodel;

public class Client {
    public static void main(String[] args) {
        MessageImplementor impl = new MessageSMS();
        AbstractMessage message = new CommonMessage(impl);
        message.sendMessage("加班申请速批", "李总");

        impl = new MessageEmail();
        message = new UrgencyMessage(impl);
        message.sendMessage("加班申请速批", "李总");
    }
}
