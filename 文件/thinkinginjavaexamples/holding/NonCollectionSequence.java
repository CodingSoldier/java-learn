package com.thinkinginjavaexamples.holding;//: holding/NonCollectionSequence.java
import com.thinkinginjavaexamples.typeinfo.pets.Pet;
import typeinfo.pets.*;
import java.util.*;

class PetSequence {
  protected com.thinkinginjavaexamples.typeinfo.pets.Pet[] pets = com.thinkinginjavaexamples.typeinfo.pets.Pets.createArray(8);
}

public class NonCollectionSequence extends PetSequence {
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
    NonCollectionSequence nc = new NonCollectionSequence();
    InterfaceVsIterator.display(nc.iterator());
  }
} /* Output:
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
*///:~
