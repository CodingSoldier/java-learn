package com.designpattern.mediator.base;

public class ConcreteMediator implements Mediator {

    private ConcreteColleagueA ca;

    private ConcreteColleagueB cb;

    public void setCA(ConcreteColleagueA ca){
        this.ca = ca;
    }

    public void setCb(ConcreteColleagueB cb){
        this.cb = cb;
    }

    @Override
    public void changed(Colleague c) {
        System.out.println("********   "+c);
    }

}



