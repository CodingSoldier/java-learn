package com.designpattern.builder.message;

public class WelcomeBuilder extends Builder {
    public WelcomeBuilder(){
        msg = new WelcomeMessage();
    }

    @Override
    public void buildSubject() {
        msg.setBody("欢迎标题");
    }

    @Override
    public void buildBody() {
        msg.setBody("欢迎内容");
    }
}
