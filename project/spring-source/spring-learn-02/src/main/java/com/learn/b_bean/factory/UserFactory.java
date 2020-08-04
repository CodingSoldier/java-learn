package com.learn.b_bean.factory;


import com.learn.a_start.User;

//实例工厂调用
public class UserFactory {
    //普通的方法，返回User对象
    //不能通过类名调用，需要通过对象调用
    public User getUser(){
        return new User();
    }

}
