//: typeinfo/pets/LiteralPetCreator.java
// Using class literals.
package com.thinkinginjavaexamples.typeinfo.pets;
import typeinfo.pets.Cat;
import typeinfo.pets.Cymric;
import typeinfo.pets.Dog;
import typeinfo.pets.EgyptianMau;
import typeinfo.pets.Hamster;
import typeinfo.pets.Manx;
import typeinfo.pets.Mouse;
import typeinfo.pets.Mutt;
import typeinfo.pets.Pet;
import typeinfo.pets.PetCreator;
import typeinfo.pets.Pug;
import typeinfo.pets.Rat;
import typeinfo.pets.Rodent;

import java.util.*;

public class LiteralPetCreator extends PetCreator {
  // No try block needed.
  @SuppressWarnings("unchecked")
  public static final List<Class<? extends typeinfo.pets.Pet>> allTypes =
    Collections.unmodifiableList(Arrays.asList(
      typeinfo.pets.Pet.class, Dog.class, Cat.class,  Rodent.class,
      typeinfo.pets.Mutt.class, Pug.class, EgyptianMau.class, Manx.class,
      Cymric.class, Rat.class, Mouse.class,Hamster.class));
  // Types for random creation:
  private static final List<Class<? extends typeinfo.pets.Pet>> types =
    allTypes.subList(allTypes.indexOf(Mutt.class),
      allTypes.size());
  public List<Class<? extends Pet>> types() {
    return types;
  }	
  public static void main(String[] args) {
    System.out.println(types);
  }
} /* Output:
[class typeinfo.pets.Mutt, class typeinfo.pets.Pug, class typeinfo.pets.EgyptianMau, class typeinfo.pets.Manx, class typeinfo.pets.Cymric, class typeinfo.pets.Rat, class typeinfo.pets.Mouse, class typeinfo.pets.Hamster]
*///:~
