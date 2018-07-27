package com.thinkinginjavablog;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Test02{

    @Test
    public void testFn01(){

        Map<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");
        map.put("key4", "val4");

        for (String key:map.keySet()){
            System.out.println("key: " + key + "  value: " + map.get(key));
        }

        for (Map.Entry<String, String> entry:map.entrySet()){
            System.out.println("key: " + entry.getKey() + "  value: " + entry.getValue());
        }

    }
}