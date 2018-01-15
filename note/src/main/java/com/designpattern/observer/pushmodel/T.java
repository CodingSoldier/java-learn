package com.designpattern.observer.pushmodel;

import org.junit.Test;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/15
 */
public class T {
    @Test
    public void t(){
        ConcreteSubject subject = new ConcreteSubject();
        Observer observer = new ConcreteObserver();
        subject.attach(observer);
        subject.change("subject的change方法执行了");
    }
}
