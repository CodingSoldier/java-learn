package com.designpattern.observer.pushmodel;

//抽象观察者角色类
public interface Observer {
    //更新状态接口
    public void update(String state);
}
