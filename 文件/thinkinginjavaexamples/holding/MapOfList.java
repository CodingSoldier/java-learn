//: holding/MapOfList.java
package com.thinkinginjavaexamples.holding;
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;
import static net.mindview.util.Print.*;

public class MapOfList {
  public static Map<com.thinkinginjavaexamples.typeinfo.pets.Person, List<? extends com.thinkinginjavaexamples.typeinfo.pets.Pet>>
    petPeople = new HashMap<com.thinkinginjavaexamples.typeinfo.pets.Person, List<? extends com.thinkinginjavaexamples.typeinfo.pets.Pet>>();
  static {
    petPeople.put(new com.thinkinginjavaexamples.typeinfo.pets.Person("Dawn"),
      Arrays.asList(new com.thinkinginjavaexamples.typeinfo.pets.Cymric("Molly"),new com.thinkinginjavaexamples.typeinfo.pets.Mutt("Spot")));
    petPeople.put(new com.thinkinginjavaexamples.typeinfo.pets.Person("Kate"),
      Arrays.asList(new com.thinkinginjavaexamples.typeinfo.pets.Cat("Shackleton"),
        new com.thinkinginjavaexamples.typeinfo.pets.Cat("Elsie May"), new com.thinkinginjavaexamples.typeinfo.pets.Dog("Margrett")));
    petPeople.put(new com.thinkinginjavaexamples.typeinfo.pets.Person("Marilyn"),
      Arrays.asList(
       new com.thinkinginjavaexamples.typeinfo.pets.Pug("Louie aka Louis Snorkelstein Dupree"),
       new com.thinkinginjavaexamples.typeinfo.pets.Cat("Stanford aka Stinky el Negro"),
       new com.thinkinginjavaexamples.typeinfo.pets.Cat("Pinkola")));
    petPeople.put(new com.thinkinginjavaexamples.typeinfo.pets.Person("Luke"),
      Arrays.asList(new com.thinkinginjavaexamples.typeinfo.pets.Rat("Fuzzy"), new com.thinkinginjavaexamples.typeinfo.pets.Rat("Fizzy")));
    petPeople.put(new com.thinkinginjavaexamples.typeinfo.pets.Person("Isaac"),
      Arrays.asList(new com.thinkinginjavaexamples.typeinfo.pets.Rat("Freckly")));
  }
  public static void main(String[] args) {
    print("People: " + petPeople.keySet());
    print("Pets: " + petPeople.values());
    for(com.thinkinginjavaexamples.typeinfo.pets.Person person : petPeople.keySet()) {
      print(person + " has:");
      for(Pet pet : petPeople.get(person))
        print("    " + pet);
    }
  }
} /* Output:	
People: [Person Luke, Person Marilyn, Person Isaac, Person Dawn, Person Kate]
Pets: [[Rat Fuzzy, Rat Fizzy], [Pug Louie aka Louis Snorkelstein Dupree, Cat Stanford aka Stinky el Negro, Cat Pinkola], [Rat Freckly], [Cymric Molly, Mutt Spot], [Cat Shackleton, Cat Elsie May, Dog Margrett]]
Person Luke has:
    Rat Fuzzy
    Rat Fizzy
Person Marilyn has:
    Pug Louie aka Louis Snorkelstein Dupree
    Cat Stanford aka Stinky el Negro
    Cat Pinkola
Person Isaac has:
    Rat Freckly
Person Dawn has:
    Cymric Molly
    Mutt Spot
Person Kate has:
    Cat Shackleton
    Cat Elsie May
    Dog Margrett
*///:~
