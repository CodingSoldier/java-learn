//: annotations/Testable.java
package com.thinkinginjavaexamples.annotations;
import net.mindview.atunit.*;

public class Testable {
  public void execute() {
    System.out.println("Executing..");
  }
  @Test void testExecute() { execute(); }
} ///:~
