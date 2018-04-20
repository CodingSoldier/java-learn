package com.thinkinginjavaexamples.access;//: access/Dinner.java
// Uses the library.
import access.dessert.*;
import com.thinkinginjavaexamples.typeinfo.Cookie;

public class Dinner {
  public static void main(String[] args) {
    Cookie x = new Cookie();
    //! x.bite(); // Can't access
  }
} /* Output:
Cookie constructor
*///:~
