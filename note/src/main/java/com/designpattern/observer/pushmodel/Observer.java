package com.designpattern.observer.pushmodel;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/15
 */
//抽象观察者角色类
public interface Observer {
    //更新状态接口
    public void update(String state);
}
