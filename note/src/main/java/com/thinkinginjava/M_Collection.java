package com.thinkinginjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/31
 */

class Snow{}
class Powder extends Snow{}
class Light extends Powder{}
class Heavy extends Powder{}
class Crusty extends Snow{}
class Slush extends Snow{}

public class M_Collection {
    @Test
    public void t(){
        List<Snow> snows = Arrays.asList(new Light(), new Heavy());
    }
}





class E05_IntegerListFeatures {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        ints.remove(1);  //优先根据index删除
        System.out.println(ints);

        ints.remove(Integer.valueOf(1)); //根据value删除
        System.out.println(ints);


        //List<int> is = new ArrayList<int>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        //报错，泛型不能是原始数据类型

    }
}





class IDClass {
    private static int counter;
    private int count = counter++;
    public String toString() {
        return "IDClass " + count;
    }
}
class E07_TestList {
    public static void main(String args[]) {
        IDClass[] idc = new IDClass[10];
        for (int i = 0; i < idc.length; i++)
            idc[i] = new IDClass();
        List<IDClass> lst = new ArrayList<IDClass>(Arrays.asList(idc));
        System.out.println("lst = " + lst);
        List<IDClass> subSet = lst.subList(lst.size() / 4, lst.size() / 2);
        System.out.println("subSet = " + subSet);
// The semantics of the sub list become undefined if the
// backing list is structurally modified!
//        lst.removeAll(subSet);
        subSet.clear();  // subList得到子集后，对子集的操作会影响到原始集合
        System.out.println("lst = " + lst);
    }
}



//230，泛型T的使用
class Stack<T> {
    private LinkedList<T> storage = new LinkedList<T>();
    public void push(T v) {
        storage.addFirst(v);  //把元素添加到开头
    }
    public T peek() {
        return storage.getFirst();
    }
    public T pop(){
        return storage.removeFirst();
    }
    public boolean empty() {
        return storage.isEmpty();
    }
    public String toString(){
        return storage.toString();
    }
}
class TT{
    public static void main(String args[]) {
        Stack<String> storage = new Stack<String>();
        storage.push("1");
        storage.push("2");
        storage.push("3");
        //storage.push(111); //new Stack的时候将泛型T指定为了String，所以push方法参数类型也被限制为String，不能push非string类型

    }
}


















