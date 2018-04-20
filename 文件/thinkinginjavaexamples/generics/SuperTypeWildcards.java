package com.thinkinginjavaexamples.generics;//: generics/SuperTypeWildcards.java
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

public class SuperTypeWildcards {
  static void writeTo(List<? super com.thinkinginjavaexamples.initialization.Apple> apples) {
    apples.add(new com.thinkinginjavaexamples.initialization.Apple());
    apples.add(new Jonathan());
    // apples.add(new Fruit()); // Error
  }
} ///:~
