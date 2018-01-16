package com.designpattern.observer.javaobserver;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/16
 */
public class Watched extends Subject {
    private String data = "";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        if(!this.data.equals(data)){
            this.data = data;
            setChanged();
        }
        notifyObservers();
    }
}
