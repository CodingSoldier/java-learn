package com.designpattern.mediator;

public class CDDriver extends Colleague {
    private String data = "";

    public CDDriver(Mediator mediator){
        super(mediator);
    }

    public String getData(){
        return data;
    }

    public void readCD(){
        this.data = "One Piece，海贼王我当定了！！";
        getMediator().changed(this);
    }

}
