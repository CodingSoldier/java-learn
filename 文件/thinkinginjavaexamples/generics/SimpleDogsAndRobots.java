package com.thinkinginjavaexamples.generics;//: generics/SimpleDogsAndRobots.java
// Removing the generic; code still works.

import com.thinkinginjavaexamples.concurrency.Robot;

class CommunicateSimply {
  static void perform(Performs performer) {
    performer.speak();
    performer.sit();
  }
}

public class SimpleDogsAndRobots {
  public static void main(String[] args) {
    CommunicateSimply.perform(new PerformingDog());
    CommunicateSimply.perform(new com.thinkinginjavaexamples.concurrency.Robot());
  }
} /* Output:
Woof!
Sitting
Click!
Clank!
*///:~
