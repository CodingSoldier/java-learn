package com.thinkinginjavaexamples.generics;//: generics/GenericWriting.java
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

public class GenericWriting {
  static <T> void writeExact(List<T> list, T item) {
    list.add(item);
  }
  static List<com.thinkinginjavaexamples.initialization.Apple> apples = new ArrayList<com.thinkinginjavaexamples.initialization.Apple>();
  static List<Fruit> fruit = new ArrayList<Fruit>();
  static void f1() {
    writeExact(apples, new com.thinkinginjavaexamples.initialization.Apple());
    // writeExact(fruit, new Apple()); // Error:
    // Incompatible types: found Fruit, required Apple
  }
  static <T> void
  writeWithWildcard(List<? super T> list, T item) {
    list.add(item);
  }	
  static void f2() {
    writeWithWildcard(apples, new com.thinkinginjavaexamples.initialization.Apple());
    writeWithWildcard(fruit, new com.thinkinginjavaexamples.initialization.Apple());
  }
  public static void main(String[] args) { f1(); f2(); }
} ///:~
