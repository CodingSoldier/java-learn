package com.designpattern.observer.pullmodel;

public class ConcreteSubject extends Subject{
    private String state;

    public String getState(){
        return state;
    }

    /**
     * 具体主题对象会存储状态这种属性变量值，抽象主题对象则存储观察者
     * 在具体主题对象中：
     * 1、change方法会改变具体主题对象本身的属性state
     * 2、且执行抽象主题对象的广播方法，
     * 3、抽象主题对象不能被new出来，new出来且能执行change方法的一定是具体主题对象，所以广播方法中观察者.update(this) 中的this就是具体主题对象，具体主题对象就能获取到change方法中改变的状态state。
     */
    public void change(String newState){
        state = newState;
        System.out.println("主题对象chang方法执行，state为:"+state);
        this.notifyObserverList();
    }
}
