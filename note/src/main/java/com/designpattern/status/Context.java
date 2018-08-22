package com.designpattern.status;

public class Context {

    private State state;

    public void setState(State state){
        this.state = state;
    }

    public void request(String sampleParam){
        state.handler(sampleParam);
    }

}
