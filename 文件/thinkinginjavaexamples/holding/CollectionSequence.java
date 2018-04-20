package com.thinkinginjavaexamples.holding;//: holding/CollectionSequence.java
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;

public class CollectionSequence
extends AbstractCollection<com.thinkinginjavaexamples.typeinfo.pets.Pet> {
  private com.thinkinginjavaexamples.typeinfo.pets.Pet[] pets = com.thinkinginjavaexamples.typeinfo.pets.Pets.createArray(8);
  public int size() { return pets.length; }
  public Iterator<com.thinkinginjavaexamples.typeinfo.pets.Pet> iterator() {
    return new Iterator<com.thinkinginjavaexamples.typeinfo.pets.Pet>() {
      private int index = 0;
      public boolean hasNext() {
        return index < pets.length;
      }
      public Pet next() { return pets[index++]; }
      public void remove() { // Not implemented
        throw new UnsupportedOperationException();
      }
    };
  }	
  public static void main(String[] args) {
    CollectionSequence c = new CollectionSequence();
    InterfaceVsIterator.display(c);
    InterfaceVsIterator.display(c.iterator());
  }
} /* Output:
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
*///:~
