package com.thinkinginjavaexamples.generics;//: generics/CheckedList.java
// Using Collection.checkedList().
import com.thinkinginjavaexamples.typeinfo.pets.Dog;
import typeinfo.pets.*;
import java.util.*;

public class CheckedList {
  @SuppressWarnings("unchecked")
  static void oldStyleMethod(List probablyDogs) {
    probablyDogs.add(new Cat());
  }	
  public static void main(String[] args) {
    List<com.thinkinginjavaexamples.typeinfo.pets.Dog> dogs1 = new ArrayList<com.thinkinginjavaexamples.typeinfo.pets.Dog>();
    oldStyleMethod(dogs1); // Quietly accepts a Cat
    List<com.thinkinginjavaexamples.typeinfo.pets.Dog> dogs2 = Collections.checkedList(
      new ArrayList<com.thinkinginjavaexamples.typeinfo.pets.Dog>(), com.thinkinginjavaexamples.typeinfo.pets.Dog.class);
    try {
      oldStyleMethod(dogs2); // Throws an exception
    } catch(Exception e) {
      System.out.println(e);
    }
    // Derived types work fine:
    List<com.thinkinginjavaexamples.typeinfo.pets.Pet> pets = Collections.checkedList(
      new ArrayList<com.thinkinginjavaexamples.typeinfo.pets.Pet>(), com.thinkinginjavaexamples.typeinfo.pets.Pet.class);
    pets.add(new Dog());
    pets.add(new Cat());
  }
} /* Output:
java.lang.ClassCastException: Attempt to insert class typeinfo.pets.Cat element into collection with element type class typeinfo.pets.Dog
*///:~
