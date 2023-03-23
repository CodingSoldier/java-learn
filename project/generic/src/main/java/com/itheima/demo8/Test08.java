package com.itheima.demo8;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * 类型通配符下限的使用
 */
public class Test08 {
    public static void main(String[] args) {
        //TreeSet<Cat> treeSet = new TreeSet<>(new Comparator2());
        TreeSet<Cat> treeSet = new TreeSet<Cat>(new Comparator1());
        treeSet.add(new Cat("jerry",20));
        treeSet.add(new Cat("amy",22));
        treeSet.add(new Cat("frank",35));
        treeSet.add(new Cat("jim",15));
        for (Cat cat : treeSet) {
            System.out.println(cat);
        }
    }
}

class Comparator1 implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        return o1.name.compareTo(o2.name);
    }
}

class Comparator2 implements Comparator<Cat> {
    @Override
    public int compare(Cat o1, Cat o2) {
        return o1.age - o2.age;
    }
}

class Comparator3 implements Comparator<MiniCat> {
    @Override
    public int compare(MiniCat o1, MiniCat o2) {
        return o1.level - o2.level;
    }
}

