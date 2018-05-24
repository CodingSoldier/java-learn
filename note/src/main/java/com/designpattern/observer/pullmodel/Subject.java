package com.designpattern.observer.pullmodel;

import java.util.ArrayList;
import java.util.List;

//抽象主题类
public abstract class Subject {
    private List<Observer> list = new ArrayList<Observer>();

    public void attach(Observer observer){
        list.add(observer);
        System.out.println("注册一个主题对象");
    }

    public void detach(Observer observer){
        list.remove(observer);
    }

    //广播的时候将主题对象广播出去给观察者，观察者可通过主题对象获取主题对象中存储的数据
    public void notifyObserverList(){
        for (Observer observer:list){
            observer.update(this);
        }
    }
}
