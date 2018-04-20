package com.thinkinginjavaexamples.generics;//: generics/CompilerIntelligence.java
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

public class CompilerIntelligence {
  public static void main(String[] args) {
    List<? extends Fruit> flist =
      Arrays.asList(new com.thinkinginjavaexamples.initialization.Apple());
    com.thinkinginjavaexamples.initialization.Apple a = (com.thinkinginjavaexamples.initialization.Apple)flist.get(0); // No warning
    flist.contains(new com.thinkinginjavaexamples.initialization.Apple()); // Argument is 'Object'
    flist.indexOf(new com.thinkinginjavaexamples.initialization.Apple()); // Argument is 'Object'
  }
} ///:~
