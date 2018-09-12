package com.demo.thread;

public class MyThreadLocal<T> extends ThreadLocal<T> {
    private T list;
    protected T initialValue(){
        return list;
    }
    //public void setList(T list){
    //    this.list = list;
    //}
    //public T get(){
    //    return list;
    //}
}
