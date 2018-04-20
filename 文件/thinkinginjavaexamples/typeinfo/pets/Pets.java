//: typeinfo/pets/Pets.java
// Facade to produce a default PetCreator.
package com.thinkinginjavaexamples.typeinfo.pets;
import typeinfo.pets.Pet;

import java.util.*;

public class Pets {
  public static final PetCreator creator =
    new LiteralPetCreator();
  public static typeinfo.pets.Pet randomPet() {
    return creator.randomPet();
  }
  public static typeinfo.pets.Pet[] createArray(int size) {
    return creator.createArray(size);
  }
  public static ArrayList<Pet> arrayList(int size) {
    return creator.arrayList(size);
  }
} ///:~
