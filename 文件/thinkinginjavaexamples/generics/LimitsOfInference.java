package com.thinkinginjavaexamples.generics;//: generics/LimitsOfInference.java
import com.thinkinginjavaexamples.typeinfo.Person;
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;

public class LimitsOfInference {
  static void
  f(Map<com.thinkinginjavaexamples.typeinfo.Person, List<? extends Pet>> petPeople) {}
  public static void main(String[] args) {
    // f(New.map()); // Does not compile
  }
} ///:~
