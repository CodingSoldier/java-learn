package com.designpattern.single;


public class SingletonClass {

    private SingletonClass(){};

    private static class SingletonHolder{
        private static SingletonClass instance = new SingletonClass();
    }

    public static SingletonClass getInstance(){
        return SingletonHolder.instance;
    }
}
