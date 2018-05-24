package com.designpattern.observer.javaobserver;

public class Watcher implements Observer {
    public Watcher(Subject o){
        o.addObserver(this);
    }

    @Override
    public void update(Subject observable, Object arg) {
        System.out.println("状态改变："+((Watched)observable).getData());
    }
}
