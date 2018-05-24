package com.designpattern.observer.pushmodel;

import org.junit.Test;

public class T {
    @Test
    public void t(){
        ConcreteSubject subject = new ConcreteSubject();
        Observer observer = new ConcreteObserver();
        subject.attach(observer);
        subject.change("subject的change方法执行了");
    }
}
