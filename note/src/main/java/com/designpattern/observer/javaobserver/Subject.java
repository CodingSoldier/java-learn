package com.designpattern.observer.javaobserver;

import java.util.Vector;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/16
 */
public class Subject {
    private boolean changed = false;
    private Vector obs;

    public Subject(){
        obs = new Vector();
    }

    public synchronized void addObserver(Observer o){
        if(o == null){
            throw new NullPointerException();
        }
        if(!obs.contains(o)){
            obs.addElement(o);
        }
    }

    public synchronized void deleteObserver(Subject o){
        obs.removeElement(o);
    }

    public void notifyObservers(){
        notifyObservers(null);
    }

    public void notifyObservers(Object arg){
        Object[] arrLocal;
        synchronized (this){
            if(!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }
        for(int i=arrLocal.length-1; i>=0; i--){
            ((Observer)arrLocal[i]).update(this, arg);
        }
    }

    public synchronized void setChanged(){
        changed = true;
    }

    protected synchronized void clearChanged(){
        changed = false;
    }

    public synchronized boolean hasChanged(){
        return changed;
    }

    public synchronized int countObservers(){
        return obs.size();
    }

}
