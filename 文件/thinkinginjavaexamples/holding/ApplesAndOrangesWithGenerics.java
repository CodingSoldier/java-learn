package com.thinkinginjavaexamples.holding;//: holding/ApplesAndOrangesWithGenerics.java
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

public class ApplesAndOrangesWithGenerics {
  public static void main(String[] args) {
    ArrayList<com.thinkinginjavaexamples.initialization.Apple> apples = new ArrayList<com.thinkinginjavaexamples.initialization.Apple>();
    for(int i = 0; i < 3; i++)
      apples.add(new com.thinkinginjavaexamples.initialization.Apple());
    // Compile-time error:
    // apples.add(new Orange());
    for(int i = 0; i < apples.size(); i++)
      System.out.println(apples.get(i).id());
    // Using foreach:
    for(com.thinkinginjavaexamples.initialization.Apple c : apples)
      System.out.println(c.id());
  }
} /* Output:
0
1
2
0
1
2
*///:~
