package com.thinkinginjava.m_collection_map;

import java.util.*;


public class M_Collection_map {
}





// 关联数组实现一个map，map数据结构：[[key0,value0],[key1,value1]]
class ArrayMap<K,V>{
    private Object[][] pairs;
    private int index;

    //新建map，须指定map的键值对数量
    public ArrayMap(int length) {
        pairs = new Object[length][2];
    }

    public void put(K key, V value) {
        if(index >= pairs.length)
            throw new ArrayIndexOutOfBoundsException();

        //put到map中的键值对，用数组存起来。[[key0,value0],[key1,value1]]
        pairs[index++] = new Object[]{ key, value };
    }

    public V get(K key) {
        for(int i = 0; i < index; i++)
            if(key.equals(pairs[i][0]))
                return (V)pairs[i][1];
        return null;
    }
}

class ArrayMapTest{
    public static void main(String[] args) {
        ArrayMap<String, String> map = new ArrayMap<String, String>(2);
        map.put("key1", "value1");
        map.put("key2", "value2");
        try {
            map.put("key3", "value3"); // 添加的键值对超过map的存储容量
        } catch(RuntimeException e) {
            System.out.println("Too many objects!\n");
        }
        System.out.println(map.get("key1"));
    }
}











// 用两个List实现map，结构如下：
// [key1, key2, key3]
// [val1, val2, val3]
class ListMap<K,V>{

    //keys存储key
    private List<K> keys = new ArrayList<K>();
    //values存储value
    private List<V> values = new ArrayList<V>();

    public V put(K key, V value) {
        if(key == null)
            throw new NullPointerException();

        V oldValue = get(key);
        if(!keys.contains(key)) {
            keys.add(key);
            values.add(value);
        } else{
            values.set(keys.indexOf(key), value);
        }

        return oldValue;
    }

    public V get(Object key) {
        if(key == null)
            throw new NullPointerException();

        if(!keys.contains(key))
            return null;

        //map的key在keys中的下标，就是对应value在values中的下标
        return values.get(keys.indexOf(key));
    }

}

class ListMapTest{
    public static void main(String[] args) {
        ListMap<String, String> map = new ListMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        System.out.println(map.get("key3"));
    }
}








//map中的每一个键值都是一个Map.Entry，我们可以实现一个自己的MapEntry
class MapEntry<K,V> implements Map.Entry<K,V> {
    private K key;
    private V value;
    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public K getKey() { return key; }
    public V getValue() { return value; }
    public V setValue(V v) {
        V result = value;
        value = v;
        return result;
    }

    public int hashCode() {
        return (key==null ? 0 : key.hashCode()) ^
            (value==null ? 0 : value.hashCode());
    }
    public boolean equals(Object o) {
        if(!(o instanceof MapEntry)) return false;
        MapEntry me = (MapEntry)o;
        return
            (key == null ?
                me.getKey() == null : key.equals(me.getKey())) &&
                (value == null ?
                    me.getValue()== null : value.equals(me.getValue()));
    }
    public String toString() { return key + "=" + value; }
}


class SimpleHashMap<K,V> extends AbstractMap<K,V> {

    static final int SIZE = 997;
    //map的底层是数组，数组长度为997。buckets中的元素称为槽或桶，buckets有997个槽
    LinkedList<MapEntry<K,V>>[] buckets = new LinkedList[SIZE];

    public V put(K key, V value) {
        V oldValue = null;
        //对key的hashcode取模，决定此key-value存储到数组的哪个槽中
        int index = Math.abs(key.hashCode()) % SIZE;
        //槽是LinkedList类型，存储MapEntry这种key-value数据
        if(buckets[index] == null)
            buckets[index] = new LinkedList<MapEntry<K,V>>();

        LinkedList<MapEntry<K,V>> bucket = buckets[index];
        MapEntry<K,V> pair = new MapEntry<K,V>(key, value);
        boolean found = false;
        ListIterator<MapEntry<K,V>> it = bucket.listIterator();
        while(it.hasNext()) {
            MapEntry<K,V> iPair = it.next();
            if(iPair.getKey().equals(key)) {
                oldValue = iPair.getValue();
                it.set(pair);
                found = true;
                break;
            }
        }
        //未找到put进来的key-value，则在槽末尾添加key-value
        if(!found)
            buckets[index].add(pair);
        return oldValue;
    }
    public V get(Object key) {
        //get操作也对key的hashcode取模，确定在哪个槽中搜索key的对应的值
        int index = Math.abs(key.hashCode()) % SIZE;
        if(buckets[index] == null) return null;
        for(MapEntry<K,V> iPair : buckets[index])
            if(iPair.getKey().equals(key))
                return iPair.getValue();
        return null;
    }

    //这有bug，仅作为演示，返回的是map键值对的副本，不是map中原有的值，对返回结果读取操作没问题，但是赋值操作将不起效果
    //java源码的entrySet是通过视图实现的。
    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> set= new HashSet<Map.Entry<K,V>>();
        for(LinkedList<MapEntry<K,V>> bucket : buckets) {
            if(bucket == null) continue;
            for(MapEntry<K,V> mpair : bucket)
                set.add(mpair);
        }
        return set;
    }

}



class SimpleHashMapTest{

    public static void main(String[] args) {
        SimpleHashMap<String,String> m = new SimpleHashMap<String,String>();
        m.put("key1", "value1");
        System.out.println(m);
        System.out.println(m.get("key1"));
        System.out.println(m.entrySet());
    }
}






















