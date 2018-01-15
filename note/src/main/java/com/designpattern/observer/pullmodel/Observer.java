package com.designpattern.observer.pullmodel;

/**
拉模式，观察者的update方法传递的参数是主题对象本身，update方法的参数固定了，且不需要传参。拉模式更好
 */
public interface Observer {
    //update方法形参是主题对象，可通过主题对象获取主题状态
    void update(Subject subject);
}
