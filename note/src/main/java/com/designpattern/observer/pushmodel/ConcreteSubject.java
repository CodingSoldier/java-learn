package com.designpattern.observer.pushmodel;

//具体观察者角色类
public class ConcreteSubject extends Subject {
    private String state;

    public String getState(){
        return state;
    }

    //在change方法中调用抽象类的广播方法
    public void change(String newState){
        this.state = newState;
        System.out.println("主题对象的change方法，主题状态为："+newState);
        this.notifyObserverList(state);
    }
}
