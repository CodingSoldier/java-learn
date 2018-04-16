package com.thinkinginjava;

import java.util.*;

public class K_Generic {
}







//P357 实现一个堆栈存储类
class LinkedStack<T> {
    private class Node {
        T item;
        Node next;
        Node() {
            item = null;
            next = null;
        }
        Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
        boolean end() {
            return item == null && next == null;
        }
    }
    private Node top = new Node(); // End sentinel
    public void push(T item) {
        top = new Node(item, top);
    }
    public T pop() {
        T result = top.item;
        if(!top.end())
            top = top.next;
        return result;
    }

    public static void main(String[] args){
        LinkedStack<String> lss = new LinkedStack<String>();
        for (String s : new String[]{"str000", "str111", "str222"}){
            lss.push(s);
        }

        String s;
        while ((s = lss.pop()) != null){
            System.out.println(s);
        }
    }
}





//  P364
class E13_OverloadedFill {
    public static <T> List<T>
    fill(List<T> list) {
        System.out.println("list");
        return list;
    }
    public static <T> Queue<T>
    fill(Queue<T> queue) {
        System.out.println("queue");
        return queue;
    }
    public static <T> LinkedList<T>
    fill(LinkedList<T> llist) {
        System.out.println("LinkedList");
        return llist;
    }
    public static <T> Set<T>
    fill(Set<T> set) {
        System.out.println("Set");
        return set;
    }
    public static void main(String[] args) {

        //调用 public static <T> List<T>
        List<String> coffeeList = fill( new ArrayList<String>() );
        fill(new ArrayList<String>());

        //调用 public static <T> Queue<T>
        Queue<Integer> iQueue = fill( (Queue<Integer>)new LinkedList<Integer>() );
        fill( (Queue<Integer>)new LinkedList<Integer>() );

        //调用 public static <T> LinkedList<T>
        LinkedList<Character> cLList = fill(  new LinkedList<Character>() );
        fill(  new LinkedList<Character>() );

        //调用 public static <T> Set<T>
        Set<Boolean> bSet = fill( new HashSet<Boolean>() );
        fill(  new HashSet<Boolean>() );

    }
}











