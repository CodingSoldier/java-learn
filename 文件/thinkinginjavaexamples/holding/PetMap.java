package com.thinkinginjavaexamples.holding;//: holding/PetMap.java
import com.thinkinginjavaexamples.generics.Cat;
import com.thinkinginjavaexamples.generics.Hamster;
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;
import static net.mindview.util.Print.*;

public class PetMap {
  public static void main(String[] args) {
    Map<String, com.thinkinginjavaexamples.typeinfo.pets.Pet> petMap = new HashMap<String, com.thinkinginjavaexamples.typeinfo.pets.Pet>();
    petMap.put("My Cat", new com.thinkinginjavaexamples.generics.Cat("Molly"));
    petMap.put("My Dog", new com.thinkinginjavaexamples.typeinfo.pets.Dog("Ginger"));
    petMap.put("My Hamster", new com.thinkinginjavaexamples.generics.Hamster("Bosco"));
    print(petMap);
    Pet dog = petMap.get("My Dog");
    print(dog);
    print(petMap.containsKey("My Dog"));
    print(petMap.containsValue(dog));
  }
} /* Output:
{My Cat=Cat Molly, My Hamster=Hamster Bosco, My Dog=Dog Ginger}
Dog Ginger
true
true
*///:~
