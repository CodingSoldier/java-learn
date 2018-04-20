package com.thinkinginjavaexamples.holding;//: holding/InterfaceVsIterator.java
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;

public class InterfaceVsIterator {
  public static void display(Iterator<com.thinkinginjavaexamples.typeinfo.pets.Pet> it) {
    while(it.hasNext()) {
      com.thinkinginjavaexamples.typeinfo.pets.Pet p = it.next();
      System.out.print(p.id() + ":" + p + " ");
    }
    System.out.println();
  }
  public static void display(Collection<com.thinkinginjavaexamples.typeinfo.pets.Pet> pets) {
    for(com.thinkinginjavaexamples.typeinfo.pets.Pet p : pets)
      System.out.print(p.id() + ":" + p + " ");
    System.out.println();
  }	
  public static void main(String[] args) {
    List<com.thinkinginjavaexamples.typeinfo.pets.Pet> petList = com.thinkinginjavaexamples.typeinfo.pets.Pets.arrayList(8);
    Set<com.thinkinginjavaexamples.typeinfo.pets.Pet> petSet = new HashSet<com.thinkinginjavaexamples.typeinfo.pets.Pet>(petList);
    Map<String, com.thinkinginjavaexamples.typeinfo.pets.Pet> petMap =
      new LinkedHashMap<String,Pet>();
    String[] names = ("Ralph, Eric, Robin, Lacey, " +
      "Britney, Sam, Spot, Fluffy").split(", ");
    for(int i = 0; i < names.length; i++)
      petMap.put(names[i], petList.get(i));
    display(petList);
    display(petSet);
    display(petList.iterator());
    display(petSet.iterator());
    System.out.println(petMap);
    System.out.println(petMap.keySet());
    display(petMap.values());
    display(petMap.values().iterator());
  }	
} /* Output:
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
4:Pug 6:Pug 3:Mutt 1:Manx 5:Cymric 7:Manx 2:Cymric 0:Rat
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
4:Pug 6:Pug 3:Mutt 1:Manx 5:Cymric 7:Manx 2:Cymric 0:Rat
{Ralph=Rat, Eric=Manx, Robin=Cymric, Lacey=Mutt, Britney=Pug, Sam=Cymric, Spot=Pug, Fluffy=Manx}
[Ralph, Eric, Robin, Lacey, Britney, Sam, Spot, Fluffy]
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
*///:~
