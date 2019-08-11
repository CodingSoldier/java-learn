package com.datastructure;

//线段树区间操作接口
public interface Merger<E>{
    E merge(E a, E b);
}