package com.thinkinginjavaexamples.holding;//: holding/LinkedListFeatures.java
import com.thinkinginjavaexamples.generics.Hamster;
import typeinfo.pets.*;
import java.util.*;
import static net.mindview.util.Print.*;

public class LinkedListFeatures {
  public static void main(String[] args) {
    LinkedList<com.thinkinginjavaexamples.typeinfo.pets.Pet> pets =
      new LinkedList<com.thinkinginjavaexamples.typeinfo.pets.Pet>(com.thinkinginjavaexamples.typeinfo.pets.Pets.arrayList(5));
    print(pets);
    // Identical:
    print("pets.getFirst(): " + pets.getFirst());
    print("pets.element(): " + pets.element());
    // Only differs in empty-list behavior:
    print("pets.peek(): " + pets.peek());
    // Identical; remove and return the first element:
    print("pets.remove(): " + pets.remove());
    print("pets.removeFirst(): " + pets.removeFirst());
    // Only differs in empty-list behavior:
    print("pets.poll(): " + pets.poll());
    print(pets);
    pets.addFirst(new com.thinkinginjavaexamples.typeinfo.pets.Rat());
    print("After addFirst(): " + pets);
    pets.offer(com.thinkinginjavaexamples.typeinfo.pets.Pets.randomPet());
    print("After offer(): " + pets);
    pets.add(com.thinkinginjavaexamples.typeinfo.pets.Pets.randomPet());
    print("After add(): " + pets);
    pets.addLast(new com.thinkinginjavaexamples.generics.Hamster());
    print("After addLast(): " + pets);
    print("pets.removeLast(): " + pets.removeLast());
  }
} /* Output:
[Rat, Manx, Cymric, Mutt, Pug]
pets.getFirst(): Rat
pets.element(): Rat
pets.peek(): Rat
pets.remove(): Rat
pets.removeFirst(): Manx
pets.poll(): Cymric
[Mutt, Pug]
After addFirst(): [Rat, Mutt, Pug]
After offer(): [Rat, Mutt, Pug, Cymric]
After add(): [Rat, Mutt, Pug, Cymric, Pug]
After addLast(): [Rat, Mutt, Pug, Cymric, Pug, Hamster]
pets.removeLast(): Hamster
*///:~
