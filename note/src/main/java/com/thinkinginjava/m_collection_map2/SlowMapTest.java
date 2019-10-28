package com.thinkinginjava.m_collection_map2;


import java.util.*;


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


class SlowMap2<K,V> extends AbstractMap<K,V> {

    private List<K> keys = new ArrayList<K>();
    private List<V> values = new ArrayList<V>();

    @Override
    public V put(K key, V value) {
        if(key == null)
            throw new NullPointerException();
        V oldValue = get(key); // The old value or null
        if(!keys.contains(key)) {
            keys.add(key);
            values.add(value);
        } else
            values.set(keys.indexOf(key), value);

        return oldValue;
    }

    @Override
    public V get(Object key) {
        if(key == null)
            throw new NullPointerException();
        if(!keys.contains(key))
            return null;

        return values.get(keys.indexOf(key));
    }

    private EntrySet entrySet = new EntrySet();

    @Override
    public Set<Entry<K,V>> entrySet() {
        return entrySet;
    }

    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            return new Iterator<Map.Entry<K,V>>() {
                private int index = -1;
                boolean canRemove;
                public boolean hasNext() {
                    return index < keys.size() - 1;
                }
                public Map.Entry<K,V> next() {
                    canRemove = true;
                    ++index;
                    return new MapEntry<K,V>(keys.get(index), values.get(index));
                }
                public void remove() {
                    if(!canRemove)
                        throw new IllegalStateException();
                    canRemove = false;
                    keys.remove(index);
                    values.remove(index--);
                }
            };
        }

        @Override
        public boolean contains(Object o) {
            //if(o instanceof MapEntry) {
            //    MapEntry<K,getNodeTotal> e = (MapEntry<K,getNodeTotal>)o;
            //    K key = e.getKey();
            //    if(keys.contains(key))
            //        return e.equals(new MapEntry<K,getNodeTotal>(key, get(key)));
            //}
            return false;
        }

        @Override
        public boolean remove(Object o) {
            //if(contains(o)) {
            //    MapEntry<K,getNodeTotal> e = (MapEntry<K,getNodeTotal>)o;
            //    K key = e.getKey();
            //    getNodeTotal val = e.getValue();
            //    keys.remove(key);
            //    values.remove(val);
            //    return true;
            //}
            return false;
        }

        @Override
        public int size() {
            return keys.size();
        }

        @Override
        public void clear() {
            //keys.clear();
            //values.clear();
        }
    }
}

public class SlowMapTest {

    public static void main(String[] args) {
        SlowMap2<String, String> map = new SlowMap2<>();

        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");
        map.put("key4", "val4");

        Set<String> keys = map.keySet();
        for (String key:keys){
            System.out.println("key: " + key + "  value: " + map.get(key));
        }

        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry:entrySet){
            System.out.println("key: " + entry.getKey() + "  value: " + entry.getValue());
        }

        HashMap<String, Object> h = new HashMap<>();
    }
}




