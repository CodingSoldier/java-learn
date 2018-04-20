package com.thinkinginjavaexamples.generics;//: generics/SelfBoundingMethods.java

import com.thinkinginjavaexamples.initialization.A;

public class SelfBoundingMethods {
  static <T extends SelfBounded<T>> T f(T arg) {
    return arg.set(arg).get();
  }
  public static void main(String[] args) {
    com.thinkinginjavaexamples.initialization.A a = f(new com.thinkinginjavaexamples.initialization.A());
  }
} ///:~
