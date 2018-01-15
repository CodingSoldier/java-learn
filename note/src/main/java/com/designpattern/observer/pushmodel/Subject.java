package com.designpattern.observer.pushmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/15
 */
//抽象主题对象，虽然是抽象类，但是却没有抽象方法
public abstract class Subject {
    //List<Observer>用于保存注册的观察者
    private List<Observer> list = new ArrayList<Observer>();

    //注册观察者对象
    public void attach(Observer observer){
        list.add(observer);
        System.out.println("主题对象注册观察者");
    }

    //删除观察者对象
    public void detach(Observer observer){
        list.remove(observer);
    }

    //广播通知所有注册的观察者对象
    public void notifyObserverList(String newState){
        for(Observer observer:list){
            observer.update(newState);
        }
    }
}
