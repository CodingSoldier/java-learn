package com.cpq.bf.a_origin.syncContainer;

import com.cpq.bf.annoations.NotThreadSafe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@NotThreadSafe
public class VectorExample2 {

    private static List<Integer> vector = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }
            Thread thread1 = new Thread() {
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }
            };
            Thread thread2 = new Thread() {
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.get(i);
                    }
                }
            };
            thread1.start();
            thread2.start();
        }
    }
}
