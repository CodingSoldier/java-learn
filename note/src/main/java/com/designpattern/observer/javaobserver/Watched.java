package com.designpattern.observer.javaobserver;

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
