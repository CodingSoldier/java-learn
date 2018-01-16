package com.designpattern.observer.javaobserver;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/16
 */
public interface Observer {
    void update(Subject observable, Object arg);
}
