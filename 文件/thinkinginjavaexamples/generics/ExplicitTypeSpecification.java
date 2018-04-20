package com.thinkinginjavaexamples.generics;//: generics/ExplicitTypeSpecification.java
import com.thinkinginjavaexamples.typeinfo.Person;
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;
import net.mindview.util.*;

public class ExplicitTypeSpecification {
  static void f(Map<com.thinkinginjavaexamples.typeinfo.Person, List<com.thinkinginjavaexamples.typeinfo.pets.Pet>> petPeople) {}
  public static void main(String[] args) {
    f(New.<com.thinkinginjavaexamples.typeinfo.Person, List<Pet>>map());
  }
} ///:~
