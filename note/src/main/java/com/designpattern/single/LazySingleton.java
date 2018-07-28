package com.designpattern.single;

public class LazySingleton {

    private volatile static LazySingleton instance = null;

    public static LazySingleton getInstance(){
        if (instance != null){
            synchronized (LazySingleton.class){
                if (instance != null){
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }

}
