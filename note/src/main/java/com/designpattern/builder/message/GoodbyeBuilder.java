package com.designpattern.builder.message;

public class GoodbyeBuilder extends Builder {
    public GoodbyeBuilder(){
        msg = new GoodbyeMessage();
    }

    @Override
    public void buildSubject() {
        msg.setSubject("欢送标题");
    }

    @Override
    public void buildBody() {
        msg.setSubject("欢送内容");
    }
}
