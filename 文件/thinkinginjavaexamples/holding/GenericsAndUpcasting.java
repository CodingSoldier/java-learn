package com.thinkinginjavaexamples.holding;//: holding/GenericsAndUpcasting.java
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

class GrannySmith extends com.thinkinginjavaexamples.initialization.Apple {}
class Gala extends com.thinkinginjavaexamples.initialization.Apple {}
class Fuji extends com.thinkinginjavaexamples.initialization.Apple {}
class Braeburn extends com.thinkinginjavaexamples.initialization.Apple {}

public class GenericsAndUpcasting {
  public static void main(String[] args) {
    ArrayList<com.thinkinginjavaexamples.initialization.Apple> apples = new ArrayList<com.thinkinginjavaexamples.initialization.Apple>();
    apples.add(new GrannySmith());
    apples.add(new Gala());
    apples.add(new Fuji());
    apples.add(new Braeburn());
    for(com.thinkinginjavaexamples.initialization.Apple c : apples)
      System.out.println(c);
  }
} /* Output: (Sample)
GrannySmith@7d772e
Gala@11b86e7
Fuji@35ce36
Braeburn@757aef
*///:~
