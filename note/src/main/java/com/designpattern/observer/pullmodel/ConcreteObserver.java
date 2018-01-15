package com.designpattern.observer.pullmodel;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/15
 */
public class ConcreteObserver implements Observer{
    //观察者状态
    private String observerState;

    @Override
    public void update(Subject subject) {
        this.observerState = ((ConcreteSubject)subject).getState();
        System.out.println("观察者的update方法通过subject实体，获取subject的状态，状态为："+observerState);
    }
}
