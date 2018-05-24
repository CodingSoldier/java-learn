package com.designpattern.observer.pullmodel;

import org.junit.Test;

public class T {
    @Test
    public void t(){
        ConcreteSubject subject = new ConcreteSubject();
        Observer observer = new ConcreteObserver();
        subject.attach(observer);
        subject.change("state改变");
    }
}
