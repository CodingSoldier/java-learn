package com.designpattern.observer.javaobserver;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/16
 */
public class Watcher implements Observer {
    public Watcher(Subject o){
        o.addObserver(this);
    }

    @Override
    public void update(Subject observable, Object arg) {
        System.out.println("状态改变："+((Watched)observable).getData());
    }
}
