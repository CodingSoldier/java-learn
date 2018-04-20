package com.thinkinginjavaexamples.holding;//: holding/CrossContainerIteration.java
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;

public class CrossContainerIteration {
  public static void display(Iterator<com.thinkinginjavaexamples.typeinfo.pets.Pet> it) {
    while(it.hasNext()) {
      com.thinkinginjavaexamples.typeinfo.pets.Pet p = it.next();
      System.out.print(p.id() + ":" + p + " ");
    }
    System.out.println();
  }	
  public static void main(String[] args) {
    ArrayList<com.thinkinginjavaexamples.typeinfo.pets.Pet> pets = com.thinkinginjavaexamples.typeinfo.pets.Pets.arrayList(8);
    LinkedList<com.thinkinginjavaexamples.typeinfo.pets.Pet> petsLL = new LinkedList<com.thinkinginjavaexamples.typeinfo.pets.Pet>(pets);
    HashSet<com.thinkinginjavaexamples.typeinfo.pets.Pet> petsHS = new HashSet<com.thinkinginjavaexamples.typeinfo.pets.Pet>(pets);
    TreeSet<com.thinkinginjavaexamples.typeinfo.pets.Pet> petsTS = new TreeSet<Pet>(pets);
    display(pets.iterator());
    display(petsLL.iterator());
    display(petsHS.iterator());
    display(petsTS.iterator());
  }
} /* Output:
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
4:Pug 6:Pug 3:Mutt 1:Manx 5:Cymric 7:Manx 2:Cymric 0:Rat
5:Cymric 2:Cymric 7:Manx 1:Manx 3:Mutt 6:Pug 4:Pug 0:Rat
*///:~
