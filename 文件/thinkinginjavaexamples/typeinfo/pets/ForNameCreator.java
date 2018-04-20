//: typeinfo/pets/ForNameCreator.java
package com.thinkinginjavaexamples.typeinfo.pets;
import typeinfo.pets.Pet;
import typeinfo.pets.PetCreator;

import java.util.*;

public class ForNameCreator extends PetCreator {
  private static List<Class<? extends typeinfo.pets.Pet>> types =
    new ArrayList<Class<? extends typeinfo.pets.Pet>>();
  // Types that you want to be randomly created:
  private static String[] typeNames = {
    "typeinfo.pets.Mutt",
    "typeinfo.pets.Pug",
    "typeinfo.pets.EgyptianMau",
    "typeinfo.pets.Manx",
    "typeinfo.pets.Cymric",
    "typeinfo.pets.Rat",
    "typeinfo.pets.Mouse",
    "typeinfo.pets.Hamster"
  };	
  @SuppressWarnings("unchecked")
  private static void loader() {
    try {
      for(String name : typeNames)
        types.add(
          (Class<? extends typeinfo.pets.Pet>)Class.forName(name));
    } catch(ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  static { loader(); }
  public List<Class<? extends Pet>> types() {return types;}
} ///:~
