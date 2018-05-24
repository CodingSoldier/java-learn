package com.designpattern.observer.pushmodel;

//具体观察者
public class ConcreteObserver implements Observer {
    //观察者状态
    private String observerState;

    //具体观察者的observerSate更新，与与目标状态保持一致
    @Override
    public void update(String state){
        this.observerState = state;
        System.out.println("观察者update方法执行，state为："+state);
    }
}
