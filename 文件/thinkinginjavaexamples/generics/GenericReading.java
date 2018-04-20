package com.thinkinginjavaexamples.generics;//: generics/GenericReading.java
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

public class GenericReading {
  static <T> T readExact(List<T> list) {
    return list.get(0);
  }
  static List<com.thinkinginjavaexamples.initialization.Apple> apples = Arrays.asList(new com.thinkinginjavaexamples.initialization.Apple());
  static List<Fruit> fruit = Arrays.asList(new Fruit());
  // A static method adapts to each call:
  static void f1() {
    com.thinkinginjavaexamples.initialization.Apple a = readExact(apples);
    Fruit f = readExact(fruit);
    f = readExact(apples);
  }
  // If, however, you have a class, then its type is
  // established when the class is instantiated:
  static class Reader<T> {
    T readExact(List<T> list) { return list.get(0); }
  }	
  static void f2() {
    Reader<Fruit> fruitReader = new Reader<Fruit>();
    Fruit f = fruitReader.readExact(fruit);
    // Fruit a = fruitReader.readExact(apples); // Error:
    // readExact(List<Fruit>) cannot be
    // applied to (List<Apple>).
  }
  static class CovariantReader<T> {
    T readCovariant(List<? extends T> list) {
      return list.get(0);
    }
  }
  static void f3() {
    CovariantReader<Fruit> fruitReader =
      new CovariantReader<Fruit>();
    Fruit f = fruitReader.readCovariant(fruit);
    Fruit a = fruitReader.readCovariant(apples);
  }	
  public static void main(String[] args) {
    f1(); f2(); f3();
  }
} ///:~
