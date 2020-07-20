package com.learn.b_ioc_learn.factory;

import com.learn.b_ioc_learn.User;

//实例工厂调用
public class UserFactory {
    //普通的方法，返回User对象
    //不能通过类名调用，需要通过对象调用
    public User getUser(){
        return new User();
    }

}
