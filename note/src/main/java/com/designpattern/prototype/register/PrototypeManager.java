package com.designpattern.prototype.register;

import java.util.HashMap;
import java.util.Map;

public class PrototypeManager {
    private static Map<String, Prototype> map = new HashMap<>();
    private PrototypeManager(){}
    public synchronized static void setPrototype(String prototypeId, Prototype prototype){
        map.put(prototypeId,prototype);
    }
    public synchronized static void removePrototype(String prototypeId){
        map.remove(prototypeId);
    }
    public synchronized static Prototype getPrototype(String prototypeId) throws RuntimeException{
        Prototype prototype1 = map.get(prototypeId);
        if (prototypeId == null){
            throw new RuntimeException("原型为空");
        }
        return prototype1;
    }
}
