package com.thinkinginjavaexamples.generics;//: generics/NonCovariantGenerics.java
// {CompileTimeError} (Won't compile)
import com.thinkinginjavaexamples.initialization.Apple;

import java.util.*;

public class NonCovariantGenerics {
  // Compile Error: incompatible types:
  List<Fruit> flist = new ArrayList<com.thinkinginjavaexamples.initialization.Apple>();
} ///:~
